package com.dongpv.sns.identity.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.dongpv.sns.identity.dto.request.auth.*;
import com.dongpv.sns.identity.exception.CommonException;
import com.dongpv.sns.identity.exception.UnauthenticatedException;
import com.dongpv.sns.identity.exception.UserNotFoundException;
import com.dongpv.sns.identity.service.AuthenticationService;
import com.dongpv.sns.identity.service.UserRefreshTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.dto.response.AuthenticationResponseDto;
import com.dongpv.sns.identity.dto.response.IntrospectResponseDto;
import com.dongpv.sns.identity.entity.InvalidatedTokenEntity;
import com.dongpv.sns.identity.repository.InvalidatedTokenRepository;
import com.dongpv.sns.identity.repository.UserRepository;
import com.dongpv.sns.identity.security.JwtTokenPrivateUtils;
import com.dongpv.sns.identity.security.JwtTokenPublicUtils;
import com.nimbusds.jose.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    @NonFinal
    @Value("${jwt.valid-duration}")
    Long jwtValidDuration;

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    UserRefreshTokenService userRefreshTokenService;
    JwtTokenPrivateUtils jwtTokenPrivateUtils;
    JwtTokenPublicUtils jwtTokenPublicUtils;
    AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto request) {
        var user = userRepository
                .findByUsernameOrEmail(request.getEmail(), request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword()));

        if (!auth.isAuthenticated()) {
            throw new UnauthenticatedException();
        }

        var token = jwtTokenPrivateUtils.generateToken(user);
        var createRefreshTokenRequestDto = CreateRefreshTokenRequestDto.builder()
                .email(user.getEmail())
                .userId(user.getId())
                .build();
        var refreshToken = userRefreshTokenService.createRefreshToken(createRefreshTokenRequestDto);
        Date expiredAt = new Date(
                Instant.now().plus(jwtValidDuration, ChronoUnit.SECONDS).toEpochMilli());

        return AuthenticationResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .expiredAt(expiredAt)
                .isAuthenticated(true)
                .build();
    }

    @Override
    public IntrospectResponseDto introspect(IntrospectRequestDto request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            jwtTokenPublicUtils.verifyToken(token);
        } catch (CommonException e) {
            isValid = false;
        }

        return IntrospectResponseDto.builder().valid(isValid).build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequestDto request) throws ParseException, JOSEException {
        try {
            var signToken = jwtTokenPublicUtils.verifyToken(request.getToken());
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedTokenEntity invalidatedTokenEntity = InvalidatedTokenEntity.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedTokenEntity);
        } catch (CommonException e) {
            LOGGER.info("Token already expired");
        }
        // Invalidate refresh token if provided
        if (request.getRefreshToken() != null && !request.getRefreshToken().isEmpty()) {
            try {
                var refreshTokenEntity = userRefreshTokenService.findByToken(request.getRefreshToken());
                userRefreshTokenService.revoke(refreshTokenEntity);
            } catch (CommonException e) {
                LOGGER.info("Refresh token already invalid or not found");
            }
        }
    }

    @Override
    public AuthenticationResponseDto refreshToken(RefreshTokenRequestDto request) {
        var refreshToken = userRefreshTokenService.findByToken(request.getRefreshToken());
        String email = userRefreshTokenService.verify(refreshToken).getEmail();
        var user = userRepository.findByEmail(email).orElseThrow(UnauthenticatedException::new);
        var token = jwtTokenPrivateUtils.generateToken(user);
        var newRefreshToken = userRefreshTokenService.rotate(refreshToken);
        Date expiredAt = new Date(
                Instant.now().plus(jwtValidDuration, ChronoUnit.SECONDS).toEpochMilli());

        return AuthenticationResponseDto.builder()
                .accessToken(token)
                .refreshToken(newRefreshToken.getToken())
                .expiredAt(expiredAt)
                .isAuthenticated(true)
                .build();
    }
}

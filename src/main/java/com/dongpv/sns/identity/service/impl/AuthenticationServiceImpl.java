package com.dongpv.sns.identity.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.dongpv.sns.identity.code.TokenType;
import com.dongpv.sns.identity.exception.CommonException;
import com.dongpv.sns.identity.exception.UnauthenticatedException;
import com.dongpv.sns.identity.exception.UserNotFoundException;
import com.dongpv.sns.identity.repository.UserRefreshTokenRepository;
import com.dongpv.sns.identity.service.AuthenticationService;
import com.dongpv.sns.identity.service.UserRefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.dto.request.auth.AuthenticationRequestDto;
import com.dongpv.sns.identity.dto.request.auth.IntrospectRequestDto;
import com.dongpv.sns.identity.dto.request.auth.LogoutRequestDto;
import com.dongpv.sns.identity.dto.request.auth.RefreshTokenRequestDto;
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
    UserRefreshTokenRepository userRefreshTokenRepository;
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
        var refreshToken = userRefreshTokenService.createRefreshToken(user.getEmail());
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
    public void logout(LogoutRequestDto request) throws ParseException, JOSEException {
        try {
            var signToken = jwtTokenPublicUtils.verifyToken(request.getToken());
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedTokenEntity invalidatedTokenEntity = InvalidatedTokenEntity.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .tokenType(TokenType.ACCESS_TOKEN.getCode())
                    .build();

            invalidatedTokenRepository.save(invalidatedTokenEntity);
        } catch (CommonException e) {
            LOGGER.info("Token already expired");
        }
        // Invalidate refresh token if provided
        if (request.getRefreshToken() != null && !request.getRefreshToken().isEmpty()) {
            try {
                var refreshTokenEntity = userRefreshTokenService.findByToken(request.getRefreshToken());
                userRefreshTokenRepository.delete(refreshTokenEntity);
            } catch (CommonException e) {
                LOGGER.info("Refresh token already invalid or not found");
            }
        }
    }

    @Override
    public AuthenticationResponseDto refreshToken(RefreshTokenRequestDto request) {
        var refreshToken = userRefreshTokenService.findByToken(request.getRefreshToken());
        String email = userRefreshTokenService.verifyExpiration(refreshToken).getEmail();
        var user = userRepository.findByEmail(email).orElseThrow(UnauthenticatedException::new);
        var token = jwtTokenPrivateUtils.generateToken(user);
        var newRefreshToken = userRefreshTokenService.createRefreshToken(email);
        Date expiredAt = new Date(
                Instant.now().plus(jwtValidDuration, ChronoUnit.SECONDS).toEpochMilli());
        userRefreshTokenRepository.deleteById(refreshToken.getId());

        return AuthenticationResponseDto.builder()
                .accessToken(token)
                .refreshToken(newRefreshToken.getToken())
                .expiredAt(expiredAt)
                .isAuthenticated(true)
                .build();
    }
}

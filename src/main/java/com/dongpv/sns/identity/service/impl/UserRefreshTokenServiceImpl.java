package com.dongpv.sns.identity.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.code.ErrorCode;
import com.dongpv.sns.identity.dto.request.auth.CreateRefreshTokenRequestDto;
import com.dongpv.sns.identity.dto.request.auth.TokenCreationResult;
import com.dongpv.sns.identity.dto.response.UserRefreshTokenResponseDto;
import com.dongpv.sns.identity.entity.UserRefreshTokenEntity;
import com.dongpv.sns.identity.exception.RefreshTokenException;
import com.dongpv.sns.identity.exception.UnauthenticatedException;
import com.dongpv.sns.identity.repository.UserRefreshTokenRepository;
import com.dongpv.sns.identity.security.JwtTokenPrivateUtils;
import com.dongpv.sns.identity.service.UserRefreshTokenService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRefreshTokenServiceImpl implements UserRefreshTokenService {
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private Long refreshTokenDuration;

    UserRefreshTokenRepository userRefreshTokenRepository;
    JwtTokenPrivateUtils jwtTokenPrivateUtils;

    @Override
    @Transactional
    public UserRefreshTokenResponseDto createRefreshToken(CreateRefreshTokenRequestDto request) {
        var refreshTokenCreation = buildRefreshTokenCreation(request);
        var refreshToken = userRefreshTokenRepository.save(refreshTokenCreation.entity());

        return UserRefreshTokenResponseDto.builder()
                .id(refreshToken.getId())
                .token(refreshTokenCreation.rawToken())
                .expiryDate(refreshToken.getExpiredAt())
                .email(refreshToken.getEmail())
                .build();
    }

    @Override
    public UserRefreshTokenEntity findByToken(String token) {
        String hashedToken = jwtTokenPrivateUtils.hashToken(token);

        return userRefreshTokenRepository.findByHashedToken(hashedToken).orElseThrow(UnauthenticatedException::new);
    }

    @Override
    @Transactional
    public UserRefreshTokenEntity verify(UserRefreshTokenEntity refreshToken) {
        if (Boolean.TRUE.equals(refreshToken.getRevoked()) || Objects.nonNull(refreshToken.getReplacedBy())) {
            throw new RefreshTokenException(ErrorCode.INVALID_OR_REVOKED_REFRESH_TOKEN.getMessage());
        }

        if (refreshToken.getExpiredAt().isBefore(Instant.now())) {
            userRefreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
        }

        return refreshToken;
    }

    @Override
    @Transactional
    public void revoke(UserRefreshTokenEntity token) {
        token.setRevoked(true);
        userRefreshTokenRepository.save(token);
    }

    @Override
    @Transactional
    public UserRefreshTokenResponseDto rotate(UserRefreshTokenEntity oldToken) {
        var refreshTokenCreation = buildRefreshTokenCreation(CreateRefreshTokenRequestDto.builder()
                .userId(oldToken.getUserId())
                .email(oldToken.getEmail())
                .deviceInfo(oldToken.getDeviceInfo())
                .build());
        var refreshToken = userRefreshTokenRepository.save(refreshTokenCreation.entity());

        oldToken.setRevoked(true);
        oldToken.setReplacedBy(refreshToken.getId());
        userRefreshTokenRepository.save(oldToken);

        return UserRefreshTokenResponseDto.builder()
                .id(refreshToken.getId())
                .token(refreshTokenCreation.rawToken())
                .expiryDate(refreshToken.getExpiredAt())
                .email(refreshToken.getEmail())
                .build();
    }

    private TokenCreationResult buildRefreshTokenCreation(CreateRefreshTokenRequestDto request) {
        String rawToken = jwtTokenPrivateUtils.generateUUIDToken();
        String hashedToken = jwtTokenPrivateUtils.hashToken(rawToken);

        UserRefreshTokenEntity refreshToken = new UserRefreshTokenEntity();
        refreshToken.setExpiredAt(Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS));
        refreshToken.setHashedToken(hashedToken);
        refreshToken.setUserId(request.getUserId());
        refreshToken.setEmail(request.getEmail());
        refreshToken.setDeviceInfo(request.getDeviceInfo());
        refreshToken.setRevoked(false);

        return new TokenCreationResult(rawToken, refreshToken);
    }
}

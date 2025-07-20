package com.dongpv.sns.identity.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.dongpv.sns.identity.code.ErrorCode;
import com.dongpv.sns.identity.exception.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.dto.response.UserRefreshTokenResponseDto;
import com.dongpv.sns.identity.entity.UserRefreshTokenEntity;
import com.dongpv.sns.identity.exception.RefreshTokenException;
import com.dongpv.sns.identity.repository.UserRefreshTokenRepository;
import com.dongpv.sns.identity.security.JwtTokenPrivateUtils;
import com.dongpv.sns.identity.service.IUserRefreshTokenService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRefreshTokenService implements IUserRefreshTokenService {
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private Long refreshTokenDuration;

    UserRefreshTokenRepository userRefreshTokenRepository;
    JwtTokenPrivateUtils jwtTokenPrivateUtils;

    @Override
    public UserRefreshTokenResponseDto createRefreshToken(String email) {
        String rawToken = jwtTokenPrivateUtils.generateUUIDToken();
        String hashedToken = jwtTokenPrivateUtils.hashToken(rawToken);

        UserRefreshTokenEntity refreshToken = new UserRefreshTokenEntity();
        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS));
        refreshToken.setHashedToken(hashedToken);
        refreshToken.setEmail(email);
        refreshToken = userRefreshTokenRepository.save(refreshToken);

        return UserRefreshTokenResponseDto.builder()
                .token(rawToken)
                .expiryDate(refreshToken.getExpiryDate())
                .email(refreshToken.getEmail())
                .build();
    }

    @Override
    public UserRefreshTokenEntity findByToken(String token) {
        String hashedToken = jwtTokenPrivateUtils.hashToken(token);

        return userRefreshTokenRepository
                .findByHashedToken(hashedToken)
                .orElseThrow(UnauthenticatedException::new);
    }

    @Override
    public UserRefreshTokenEntity verifyExpiration(UserRefreshTokenEntity refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            userRefreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
        }
        return refreshToken;
    }
}

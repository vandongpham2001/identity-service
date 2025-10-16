package com.dongpv.sns.identity.service;

import com.dongpv.sns.identity.dto.request.auth.CreateRefreshTokenRequestDto;
import com.dongpv.sns.identity.dto.response.UserRefreshTokenResponseDto;
import com.dongpv.sns.identity.entity.UserRefreshTokenEntity;

public interface UserRefreshTokenService {
    UserRefreshTokenResponseDto createRefreshToken(CreateRefreshTokenRequestDto request);

    UserRefreshTokenEntity findByToken(String token);

    UserRefreshTokenEntity verify(UserRefreshTokenEntity token);

    void revoke(UserRefreshTokenEntity token);

    UserRefreshTokenResponseDto rotate(UserRefreshTokenEntity oldToken);
}

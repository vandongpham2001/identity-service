package com.dongpv.sns.identity.service;

import com.dongpv.sns.identity.dto.response.UserRefreshTokenResponseDto;
import com.dongpv.sns.identity.entity.UserRefreshTokenEntity;

public interface IUserRefreshTokenService {
    UserRefreshTokenResponseDto createRefreshToken(String email);
    UserRefreshTokenEntity findByToken(String token);
    UserRefreshTokenEntity verifyExpiration(UserRefreshTokenEntity token);
}

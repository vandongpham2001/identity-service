package com.dongpv.sns.identity.dto.request.auth;

import com.dongpv.sns.identity.entity.UserRefreshTokenEntity;

public record TokenCreationResult(String rawToken, UserRefreshTokenEntity entity) {}

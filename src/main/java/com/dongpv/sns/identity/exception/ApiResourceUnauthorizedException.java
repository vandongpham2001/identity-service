package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class ApiResourceUnauthorizedException extends RuntimeException {
    public ApiResourceUnauthorizedException() {
        this(ErrorCode.API_RESOURCE_UNAUTHORIZED.getMessage());
    }

    public ApiResourceUnauthorizedException(String message) {
        super(message);
    }
}

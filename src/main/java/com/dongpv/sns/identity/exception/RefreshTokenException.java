package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException() {
        this(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
    }
    
    public RefreshTokenException(String message) {
        super(String.format(message));
    }
}

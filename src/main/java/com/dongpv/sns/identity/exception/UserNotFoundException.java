package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(String.format(message));
    }

    public UserNotFoundException() {
        this(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}

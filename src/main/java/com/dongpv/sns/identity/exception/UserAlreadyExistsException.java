package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(String.format(message));
    }

    public UserAlreadyExistsException() {
        this(ErrorCode.USER_ALREADY_EXISTS.getMessage());
    }
}

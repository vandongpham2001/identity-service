package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(String.format(message));
    }

    public UserExistException() {
        this(ErrorCode.USER_EXISTED.getMessage());
    }
}

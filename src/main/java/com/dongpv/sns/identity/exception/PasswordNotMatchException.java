package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException() {
        this(ErrorCode.PASSWORD_NOT_MATCH.getMessage());
    }

    public PasswordNotMatchException(String message) {
        super(String.format(message));
    }
}

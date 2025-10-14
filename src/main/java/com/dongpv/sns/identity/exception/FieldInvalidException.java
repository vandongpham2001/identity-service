package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class FieldInvalidException extends RuntimeException {
    public FieldInvalidException() {
        this(ErrorCode.INVALID_FIELD.getMessage());
    }

    public FieldInvalidException(String message) {
        super(String.format(message));
    }

    public FieldInvalidException(String message, Object... args) {
        super(String.format(message, args));
    }
}

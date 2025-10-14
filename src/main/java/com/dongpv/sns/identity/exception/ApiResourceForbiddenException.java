package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class ApiResourceForbiddenException extends RuntimeException {
    public ApiResourceForbiddenException() {
        this(ErrorCode.API_RESOURCE_FORBIDDEN.getMessage());
    }

    public ApiResourceForbiddenException(String message) {
        super(message);
    }
}

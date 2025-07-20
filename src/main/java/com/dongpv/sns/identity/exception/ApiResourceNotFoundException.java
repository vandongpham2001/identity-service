package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

import java.io.Serial;

public class ApiResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3670539246776446255L;

    public ApiResourceNotFoundException() {
        this(ErrorCode.API_RESOURCE_NOT_FOUND.getMessage());
    }

    public ApiResourceNotFoundException(String message) {
        super(message);
    }
}

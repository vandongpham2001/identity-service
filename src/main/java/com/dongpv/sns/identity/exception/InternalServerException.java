package com.dongpv.sns.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dongpv.sns.identity.code.ErrorCode;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InternalServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InternalServerException() {
        this(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    public InternalServerException(String message) {
        super(String.format(message));
    }
}

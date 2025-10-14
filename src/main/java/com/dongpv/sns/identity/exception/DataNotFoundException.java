package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import java.io.Serial;

public class DataNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DataNotFoundException() {
        this(ErrorCode.DATA_NOT_FOUND.getMessage());
    }

    public DataNotFoundException(String message) {
        super(String.format(message));
    }
}

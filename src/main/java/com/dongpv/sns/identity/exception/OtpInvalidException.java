package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class OtpInvalidException extends RuntimeException {
    public OtpInvalidException() {
        this(ErrorCode.INVALID_OTP.getMessage());
    }

    public OtpInvalidException(String message) {
        super(String.format(message));
    }
}

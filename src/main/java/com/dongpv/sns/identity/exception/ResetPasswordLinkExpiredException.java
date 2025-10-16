package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class ResetPasswordLinkExpiredException extends RuntimeException {
    public ResetPasswordLinkExpiredException() {
        this(ErrorCode.RESET_PASSWORD_LINK_EXPIRED.getMessage());
    }

    public ResetPasswordLinkExpiredException(String message) {
        super(String.format(message));
    }
}

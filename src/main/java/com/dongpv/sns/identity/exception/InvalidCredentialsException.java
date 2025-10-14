package com.dongpv.sns.identity.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(String.format(message));
    }
}

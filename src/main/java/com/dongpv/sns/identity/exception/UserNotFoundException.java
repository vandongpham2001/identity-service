package com.dongpv.sns.identity.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(String.format(message));
    }

    public UserNotFoundException() {
        super();
    }
}

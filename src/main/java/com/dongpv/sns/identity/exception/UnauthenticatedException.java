package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import java.io.Serial;

/**
 * @author DongPV
 */
public class UnauthenticatedException extends RuntimeException {

    /** */
    @Serial
    private static final long serialVersionUID = 564402271320960298L;

    /** */
    public UnauthenticatedException() {
        this(ErrorCode.UNAUTHENTICATED.getMessage());
    }

    /**
     * @param message
     */
    public UnauthenticatedException(String message) {
        super(message);
    }
}

package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import java.io.Serial;

/**
 * @author DongPV
 */
public class UnauthorizedUserException extends RuntimeException {

    /** */
    @Serial
    private static final long serialVersionUID = 564402271320960298L;

    /** */
    public UnauthorizedUserException() {
        this(ErrorCode.UNAUTHORIZED.getMessage());
    }

    /**
     * @param message
     */
    public UnauthorizedUserException(String message) {
        super(message);
    }
}

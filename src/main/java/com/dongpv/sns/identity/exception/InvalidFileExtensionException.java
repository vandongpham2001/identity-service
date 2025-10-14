package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

/**
 * @author DongPV
 */
public class InvalidFileExtensionException extends RuntimeException {

    /** */
    private static final long serialVersionUID = -2765745604425986829L;

    /** */
    public InvalidFileExtensionException() {
        this(ErrorCode.INVALID_FILE_EXTENSION.getMessage());
    }

    /**
     * @param message
     */
    public InvalidFileExtensionException(String message) {
        super(message);
    }
}

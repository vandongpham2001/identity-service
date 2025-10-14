package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

/**
 * @author DongPV
 */
public class FileStorageException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 2714672004902751973L;

    /** */
    public FileStorageException() {
        this(ErrorCode.FILE_STORAGE_ERROR.getMessage());
    }

    /**
     * @param message String
     */
    public FileStorageException(String message) {
        super(message);
    }

    /**
     * @param message String
     * @param ex Exception
     */
    public FileStorageException(String message, Exception ex) {
        super(message, ex);
    }
}

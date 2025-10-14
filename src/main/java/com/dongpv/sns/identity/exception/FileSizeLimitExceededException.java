package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

/**
 * @author DongPV
 */
public class FileSizeLimitExceededException extends RuntimeException {

    /** */
    private static final long serialVersionUID = -8283893685394115712L;

    /** */
    public FileSizeLimitExceededException() {
        this(ErrorCode.FILE_SIZE_LIMIT_EXCEEDED.getMessage());
    }

    /**
     * @param message
     */
    public FileSizeLimitExceededException(String message) {
        super(message);
    }
}

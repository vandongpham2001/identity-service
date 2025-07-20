package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

/**
 * @author DongPV
 */
public class InvalidFileContentTypeException extends RuntimeException {

    /** */
    private static final long serialVersionUID = -8283893685394115712L;

    /** */
    public InvalidFileContentTypeException() {
        this(ErrorCode.INVALID_FILE_CONTENT_TYPE.getMessage());
    }

    /**
     * @param mesage
     */
    public InvalidFileContentTypeException(String mesage) {
        super(mesage);
    }
}

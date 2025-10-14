package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

/**
 * @author DongPV
 */
public class FolderAlreadyExistsException extends RuntimeException {

    /** */
    private static final long serialVersionUID = -5031930672336923314L;

    /** */
    public FolderAlreadyExistsException() {
        this(ErrorCode.FOLDER_ALREADY_EXISTS.getMessage());
    }

    /**
     * @param message
     */
    public FolderAlreadyExistsException(String message) {
        super(message);
    }
}

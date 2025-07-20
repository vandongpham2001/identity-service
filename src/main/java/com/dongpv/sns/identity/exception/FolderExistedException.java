package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

/**
 * @author DongPV
 */
public class FolderExistedException extends RuntimeException {

    /** */
    private static final long serialVersionUID = -5031930672336923314L;

    /** */
    public FolderExistedException() {
        this(ErrorCode.FOLDER_EXISTED.getMessage());
    }

    /**
     * @param message
     */
    public FolderExistedException(String message) {
        super(message);
    }
}

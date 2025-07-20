package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class FolderNotExistedException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 3670539246776446255L;

    /** */
    public FolderNotExistedException() {
        this(ErrorCode.FOLDER_NOT_EXISTED.getMessage());
    }

    /**
     * @param message
     */
    public FolderNotExistedException(String message) {
        super(message);
    }
}

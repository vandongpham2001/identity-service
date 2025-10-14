package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;

public class FolderNotFoundException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 3670539246776446255L;

    /** */
    public FolderNotFoundException() {
        this(ErrorCode.FOLDER_NOT_FOUND.getMessage());
    }

    /**
     * @param message
     */
    public FolderNotFoundException(String message) {
        super(message);
    }
}

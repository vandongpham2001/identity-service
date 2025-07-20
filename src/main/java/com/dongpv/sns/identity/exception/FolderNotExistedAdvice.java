package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dongpv.sns.identity.dto.BaseApiResponse;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_EXCEPTION;

/**
 * @author DongPV
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class FolderNotExistedAdvice {

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler(FolderNotExistedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final BaseApiResponse handleFolderDoesNotExistException(FolderNotExistedException ex) {
        final MultiRecordErrorResponseDtoBase response = new MultiRecordErrorResponseDtoBase(
                ErrorCode.FOLDER_NOT_EXISTED.getCode(), HttpStatus.BAD_REQUEST.getReasonPhrase());

        response.addFirstRecordDetail(KEY_EXCEPTION, ex.getLocalizedMessage());

        return response;
    }
}

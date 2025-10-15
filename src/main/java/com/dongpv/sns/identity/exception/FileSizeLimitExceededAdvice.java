package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.dongpv.sns.identity.dto.BaseApiResponse;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_EXCEPTION;

/**
 * @author DongPV
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FileSizeLimitExceededAdvice {

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler({FileSizeLimitExceededException.class, MaxUploadSizeExceededException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final BaseApiResponse handleFileNotSavedException(Exception ex) {
        final MultiRecordErrorResponseDtoBase response = new MultiRecordErrorResponseDtoBase(
                ErrorCode.FILE_SIZE_LIMIT_EXCEEDED.getCode(), HttpStatus.BAD_REQUEST.getReasonPhrase());

        response.addFirstRecordDetail(KEY_EXCEPTION, ex.getLocalizedMessage());

        return response;
    }
}

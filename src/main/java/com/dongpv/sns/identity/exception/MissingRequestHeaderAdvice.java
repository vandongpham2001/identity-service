package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dongpv.sns.identity.dto.BaseApiResponse;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_EXCEPTION;

/**
 * @author DongPV
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MissingRequestHeaderAdvice {

    /**
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public final BaseApiResponse handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        final MultiRecordErrorResponseDtoBase response = new MultiRecordErrorResponseDtoBase(
                ErrorCode.INVALID_FIELD.getCode(), HttpStatus.BAD_REQUEST.getReasonPhrase());

        response.addFirstRecordDetail(KEY_EXCEPTION, ex.getLocalizedMessage());

        return response;
    }
}

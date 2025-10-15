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

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_MESSAGE;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataNotFoundAdvice {
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final BaseApiResponse handleDataNotFoundException(DataNotFoundException ex) {
        final MultiRecordErrorResponseDtoBase response =
                new MultiRecordErrorResponseDtoBase(ErrorCode.DATA_NOT_FOUND.getCode(), ex.getMessage());
        response.addFirstRecordDetail(KEY_MESSAGE, ex.getLocalizedMessage());
        return response;
    }
}

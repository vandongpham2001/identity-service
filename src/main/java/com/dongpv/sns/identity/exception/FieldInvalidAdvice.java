package com.dongpv.sns.identity.exception;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_MESSAGE;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dongpv.sns.identity.code.ErrorCode;
import com.dongpv.sns.identity.dto.BaseApiResponse;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FieldInvalidAdvice {
    @ExceptionHandler(FieldInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final BaseApiResponse handleFieldInvalidException(FieldInvalidException ex) {
        final MultiRecordErrorResponseDtoBase response =
                new MultiRecordErrorResponseDtoBase(ErrorCode.INVALID_FIELD.getCode(), ex.getMessage());
        response.addFirstRecordDetail(KEY_MESSAGE, ex.getLocalizedMessage());
        return response;
    }
}

package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dongpv.sns.identity.dto.BaseApiResponse;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_ID;

@RestControllerAdvice
public class EntityNotFoundAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final BaseApiResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        MultiRecordErrorResponseDtoBase errorResponse =
                new MultiRecordErrorResponseDtoBase(ErrorCode.ENTITY_NOT_FOUND.getCode(), ex.getMessage());
        errorResponse.addFirstRecordDetail(KEY_ID, ex.getMessage());
        return errorResponse;
    }
}

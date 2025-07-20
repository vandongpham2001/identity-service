package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dongpv.sns.identity.dto.BaseApiResponse;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_MESSAGE;

@RestControllerAdvice
public class DataExistedAdvice {
    @ExceptionHandler(DataExistedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public final BaseApiResponse handleDataExistedException(DataExistedException ex) {
        final MultiRecordErrorResponseDtoBase response =
                new MultiRecordErrorResponseDtoBase(ErrorCode.DATA_EXISTED.getCode(), ex.getMessage());
        response.addFirstRecordDetail(KEY_MESSAGE, ex.getLocalizedMessage());
        return response;
    }
}

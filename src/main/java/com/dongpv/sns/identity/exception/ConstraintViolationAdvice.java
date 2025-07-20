package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dongpv.sns.identity.dto.BaseApiResponse;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;

import lombok.extern.slf4j.Slf4j;

/**
 * @author DongPV
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ConstraintViolationAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseApiResponse handleConstraintViolationException(ConstraintViolationException ex) {
        final MultiRecordErrorResponseDtoBase errorResponse = new MultiRecordErrorResponseDtoBase(
                ErrorCode.INVALID_FIELD.getCode(), HttpStatus.BAD_REQUEST.getReasonPhrase());

        final Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        for (final ConstraintViolation<?> error : violations) {
            LOGGER.debug(error.getPropertyPath() + ": " + error.getMessage());
            errorResponse.addFirstRecordDetail(
                    getFieldName(error.getPropertyPath().toString()),
                    getPrefix(error.getPropertyPath().toString()) + error.getMessage());
        }

        return errorResponse;
    }

    /**
     * @param fieldPath
     * @return
     */
    private String getFieldName(String fieldPath) {

        if (fieldPath.startsWith("logicalSystemId")) {
            return "logicalSystemId";

        } else if (fieldPath.startsWith("logicalConnectId")) {
            return "logicalConnectId";

        } else if (fieldPath.startsWith("logicalUnitId")) {
            return "logicalUnitId";

        } else if (fieldPath.startsWith("physicalUnitId")) {
            return "physicalUnitId";

        } else if (fieldPath.startsWith("physicalPinId")) {
            return "physicalPinId";

        } else if (fieldPath.startsWith("physicalConnectId")) {
            return "physicalConnectId";

        } else if (fieldPath.startsWith("logicalPinId")) {
            return "logicalPinId";

        } else if (fieldPath.startsWith("libraryMpCategoryId")) {
            return "libraryMpCategoryId";

        } else if (fieldPath.startsWith("libraryUnitId")) {
            return "libraryUnitId";

        } else if (fieldPath.startsWith("key")) {
            return "key";

        } else if (fieldPath.startsWith("value")) {
            return "value";

        } else if (fieldPath.equals("")) {
            return "request";
        }

        return fieldPath;
    }

    /**
     * @param fieldPath
     * @return
     */
    private String getPrefix(String fieldPath) {

        if (!fieldPath.startsWith("series") && fieldPath.endsWith("Name")) {
            return "field name ";

        } else if (fieldPath.endsWith("Val")) {
            return "field value ";
        }

        return "";
    }
}

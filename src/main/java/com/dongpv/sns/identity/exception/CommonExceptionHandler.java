package com.dongpv.sns.identity.exception;

import com.dongpv.sns.identity.code.ErrorCode;
import jakarta.xml.bind.UnmarshalException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dongpv.sns.identity.dto.BaseApiResponse;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;

import lombok.extern.slf4j.Slf4j;

/**
 * @author DongPV
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String KEY_EXCEPTION = "exception";
    private static final String LOG_TEMPLATE = "{}::{}() - {}";

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final BaseApiResponse handleMultipartException(MultipartException ex) {
        final MultiRecordErrorResponseDtoBase error = new MultiRecordErrorResponseDtoBase(
                ErrorCode.INVALID_FIELD.getCode(), HttpStatus.BAD_REQUEST.getReasonPhrase());

        error.addRecordDetail(KEY_EXCEPTION, ex.getLocalizedMessage());
        return error;
    }

    @ExceptionHandler(UnmarshalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUnmarshalException(UnmarshalException ex) {
        LOGGER.error(
                LOG_TEMPLATE,
                getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage());
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(HttpStatus.valueOf(status.value()), ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(HttpStatus.valueOf(status.value()), ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(HttpStatus.valueOf(status.value()), ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(HttpStatus.valueOf(status.value()), ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final MultiRecordErrorResponseDtoBase response = new MultiRecordErrorResponseDtoBase(
                status.value(), HttpStatus.valueOf(status.value()).getReasonPhrase());
        setFieldErrors(ex.getBindingResult(), response);

        return new ResponseEntity<>(response, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        return getResponseEntity(HttpStatus.valueOf(status.value()), ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(HttpStatus.valueOf(status.value()), ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(HttpStatus.valueOf(status.value()), ex.getLocalizedMessage(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(HttpStatus.valueOf(status.value()), ex.getLocalizedMessage(), headers);
    }

    private final ResponseEntity<Object> getResponseEntity(HttpStatus status, String message) {
        return getResponseEntity(status, message, new HttpHeaders());
    }

    private final ResponseEntity<Object> getResponseEntity(HttpStatus status, String message, HttpHeaders headers) {
        final MultiRecordErrorResponseDtoBase response =
                new MultiRecordErrorResponseDtoBase(status.value(), status.getReasonPhrase());

        response.addFirstRecordDetail(KEY_EXCEPTION, message);
        return new ResponseEntity<>(response, headers, status);
    }

    private void setFieldErrors(BindingResult bindingResult, MultiRecordErrorResponseDtoBase response) {

        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            final String field = fieldError.getField();
            final String objectName = fieldError.getObjectName();
            String message = fieldError.getDefaultMessage();

            if (message != null) {
                message = message.replaceAll(field + " ", "");
            }

            if (objectName.contains("_")) {
                message = objectName + " " + message;
            }

            response.addFirstRecordDetail(field, message);
        }
    }
}

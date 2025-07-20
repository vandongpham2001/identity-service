package com.dongpv.sns.identity.code;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // System Errors
    UNCATEGORIZED_EXCEPTION(1000, "Uncategorized error.", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR(1001, "Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1002, "Uncategorized error.", HttpStatus.BAD_REQUEST),

    // Authentication & Authorization Errors
    UNAUTHENTICATED(1100, "User is not authenticated.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1101, "User is not authorized to access this resource.", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1102, "Invalid username or password.", HttpStatus.UNAUTHORIZED),
    API_RESOURCE_UNAUTHORIZED(1103, "API resource access is unauthorized.", HttpStatus.UNAUTHORIZED),
    API_RESOURCE_FORBIDDEN(1104, "API resource access is forbidden.", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(1105, "User not found.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1106, "Invalid token.", HttpStatus.UNAUTHORIZED),

    // User Management Errors
    USER_EXISTED(1200, "User already existed.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1201, "User not existed.", HttpStatus.NOT_FOUND),

    // Data Management Errors
    DATA_NOT_FOUND(1300, "Data not found.", HttpStatus.NOT_FOUND),
    DATA_EXISTED(1301, "Data already existed.", HttpStatus.UNPROCESSABLE_ENTITY),
    API_RESOURCE_NOT_FOUND(1302, "API resource not found.", HttpStatus.NOT_FOUND),
    ENTITY_NOT_FOUND(1303, "Entity not found.", HttpStatus.NOT_FOUND),

    // Validation Errors
    INVALID_FIELD(1400, "Invalid field data.", HttpStatus.BAD_REQUEST),
    INVALID_OTP(1401, "Invalid OTP code.", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1402, "Password does not match.", HttpStatus.BAD_REQUEST),
    RESET_PASSWORD_LINK_EXPIRED(1403, "Reset password link has expired.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_EXPIRED(1404, "Refresh token has expired.", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1405, "Username must be at least {min} characters.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1406, "Password must be at least {min} characters.", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1407, "Your age must be at least {min}.", HttpStatus.BAD_REQUEST),

    // File Management Errors
    FILE_STORAGE_ERROR(1500, "File storage error occurred.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_SIZE_LIMIT_EXCEEDED(1501, "File size exceeds the maximum limit.", HttpStatus.BAD_REQUEST),
    INVALID_FILE_EXTENSION(1502, "Invalid file extension.", HttpStatus.BAD_REQUEST),
    INVALID_FILE_CONTENT_TYPE(1503, "Invalid file content type.", HttpStatus.BAD_REQUEST),
    FOLDER_EXISTED(1504, "Folder already existed.", HttpStatus.BAD_REQUEST),
    FOLDER_NOT_EXISTED(1505, "Folder does not existed.", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}

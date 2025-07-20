package com.dongpv.sns.identity.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.dongpv.sns.identity.code.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;
    private static final String ATTRIBUTE_MIN = "min";
    private static final String PLACEHOLDER_PATTERN = "{" + ATTRIBUTE_MIN + "}";

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(localDate)) {
            return true;
        }
        long years = ChronoUnit.YEARS.between(localDate, LocalDate.now());

        if (years >= min) {
            return true;
        }
        // Interpolate min value into the message
        constraintValidatorContext.disableDefaultConstraintViolation();
        var messageTemplate = constraintValidatorContext.getDefaultConstraintMessageTemplate();
        
        String message = buildValidationMessage(messageTemplate);
        
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
    
    /**
     * Builds the validation message by replacing placeholders with actual values.
     * 
     * @param messageTemplate the template message from the constraint
     * @return the formatted validation message
     */
    private String buildValidationMessage(String messageTemplate) {
        if (messageTemplate == null || messageTemplate.trim().isEmpty()) {
            return ErrorCode.INVALID_DOB.getMessage().replace(PLACEHOLDER_PATTERN, String.valueOf(min));
        }
        
        try {
            var errorCode = ErrorCode.valueOf(messageTemplate);
            return errorCode.getMessage().replace(PLACEHOLDER_PATTERN, String.valueOf(min));
        } catch (IllegalArgumentException ignored) {
            // Fall back to default DOB error message if messageTemplate is not a valid ErrorCode
            return ErrorCode.INVALID_DOB.getMessage().replace(PLACEHOLDER_PATTERN, String.valueOf(min));
        }
    }
}

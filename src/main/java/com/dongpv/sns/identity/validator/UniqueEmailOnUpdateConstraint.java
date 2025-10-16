package com.dongpv.sns.identity.validator;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueEmailOnUpdateValidator.class})
public @interface UniqueEmailOnUpdateConstraint {

    String message() default "Email already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String emailField() default "email";

    String idField() default "id";
}

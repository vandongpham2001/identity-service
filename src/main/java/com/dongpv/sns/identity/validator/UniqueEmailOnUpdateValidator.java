package com.dongpv.sns.identity.validator;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import com.dongpv.sns.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UniqueEmailOnUpdateValidator implements ConstraintValidator<UniqueEmailOnUpdateConstraint, Object> {

    UserRepository userRepository;

    @NonFinal
    String emailField;

    @NonFinal
    String idField;

    @Override
    public void initialize(UniqueEmailOnUpdateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.emailField = constraintAnnotation.emailField();
        this.idField = constraintAnnotation.idField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        String email = (String) new BeanWrapperImpl(value).getPropertyValue(emailField);
        String userId = (String) new BeanWrapperImpl(value).getPropertyValue(idField);

        if (Objects.isNull(email)) {
            return true;
        }

        var existingUser = userRepository.findByEmail(email).orElse(null);
        if (Objects.isNull(existingUser) || Objects.equals(existingUser.getId(), userId)) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
                .buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                .addPropertyNode(emailField)
                .addConstraintViolation();

        return false;
    }
}

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
public class UniqueUsernameOnUpdateValidator implements ConstraintValidator<UniqueUsernameOnUpdateConstraint, Object> {

    UserRepository userRepository;

    @NonFinal
    String usernameField;

    @NonFinal
    String idField;

    @Override
    public void initialize(UniqueUsernameOnUpdateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.usernameField = constraintAnnotation.usernameField();
        this.idField = constraintAnnotation.idField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        String username = (String) new BeanWrapperImpl(value).getPropertyValue(usernameField);
        String userId = (String) new BeanWrapperImpl(value).getPropertyValue(idField);

        if (Objects.isNull(username)) {
            return true;
        }

        var existingUser = userRepository.findByUsername(username).orElse(null);
        if (Objects.isNull(existingUser) || Objects.equals(existingUser.getId(), userId)) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
                .buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                .addPropertyNode(usernameField)
                .addConstraintViolation();

        return false;
    }
}

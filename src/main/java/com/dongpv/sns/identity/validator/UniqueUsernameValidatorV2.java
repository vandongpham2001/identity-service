package com.dongpv.sns.identity.validator;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.dongpv.sns.identity.dto.request.admin.user.UpdateUserRequestDto;
import com.dongpv.sns.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UniqueUsernameValidatorV2
        implements ConstraintValidator<UniqueUsernameConstraintV2, UpdateUserRequestDto> {

    UserRepository userRepository;
    private static final String ATTRIBUTE_MIN = "min";

    @Override
    public void initialize(UniqueUsernameConstraintV2 constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateUserRequestDto value, ConstraintValidatorContext constraintValidatorContext) {
        var username = value.getUsername();
        var userId = value.getId();

        if (username == null || username.trim().isEmpty()) {
            return true;
        }

        var existedUser = userRepository.findByUsername(username).orElse(null);
        if (Objects.isNull(existedUser) || Objects.equals(existedUser.getId(), userId)) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext
                .buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                .addPropertyNode(ATTRIBUTE_MIN)
                .addConstraintViolation();

        return false;
    }
}

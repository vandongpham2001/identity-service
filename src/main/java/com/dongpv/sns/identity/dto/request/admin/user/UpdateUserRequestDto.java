package com.dongpv.sns.identity.dto.request.admin.user;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.*;

import com.dongpv.sns.identity.code.Gender;
import com.dongpv.sns.identity.validator.DobConstraint;
import com.dongpv.sns.identity.validator.UniqueEmailOnUpdateConstraint;
import com.dongpv.sns.identity.validator.UniqueUsernameOnUpdateConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@UniqueEmailOnUpdateConstraint(message = "EMAIL_ALREADY_EXISTS")
@UniqueUsernameOnUpdateConstraint(message = "USERNAME_ALREADY_EXISTS")
public class UpdateUserRequestDto {
    @NotBlank
    String id;

    @Email
    @NotBlank
    String email;

    @NotBlank
    @Size(min = 3, message = "INVALID_USERNAME")
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD_TOO_SHORT")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$", message = "INVALID_PASSWORD_FORMAT")
    String password;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotNull
    @DobConstraint(min = 14, message = "INVALID_DOB")
    LocalDate dob;

    @NotNull
    Gender gender;

    List<String> roles;
}

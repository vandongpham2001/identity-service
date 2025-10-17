package com.dongpv.sns.identity.dto.request.admin.user;

import java.time.LocalDate;
import java.util.List;

import com.dongpv.sns.identity.code.Gender;
import jakarta.validation.constraints.*;

import com.dongpv.sns.identity.validator.DobConstraint;
import com.dongpv.sns.identity.validator.UniqueEmailOnCreateConstraint;
import com.dongpv.sns.identity.validator.UniqueUsernameOnCreateConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequestDto {
    @Email
    @NotBlank
    @UniqueEmailOnCreateConstraint(message = "EMAIL_ALREADY_EXISTS")
    String email;

    @NotBlank
    @Size(min = 3, message = "INVALID_USERNAME")
    @UniqueUsernameOnCreateConstraint(message = "USERNAME_ALREADY_EXISTS")
    String username;

    @NotBlank
    @Size(min = 8, message = "INVALID_PASSWORD_TOO_SHORT")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "INVALID_PASSWORD_FORMAT"
    )
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

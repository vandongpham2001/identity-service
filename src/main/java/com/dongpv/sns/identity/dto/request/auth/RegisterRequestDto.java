package com.dongpv.sns.identity.dto.request.auth;

import com.dongpv.sns.identity.code.Gender;
import com.dongpv.sns.identity.validator.DobConstraint;
import com.dongpv.sns.identity.validator.UniqueEmailOnCreateConstraint;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequestDto {
    @Email
    @NotBlank
    @UniqueEmailOnCreateConstraint(message = "EMAIL_ALREADY_EXISTS")
    String email;

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
}

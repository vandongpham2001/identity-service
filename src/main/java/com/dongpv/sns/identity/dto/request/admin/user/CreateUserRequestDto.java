package com.dongpv.sns.identity.dto.request.admin.user;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import com.dongpv.sns.identity.validator.DobConstraint;
import com.dongpv.sns.identity.validator.UniqueEmailConstraint;
import com.dongpv.sns.identity.validator.UniqueUsernameConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequestDto {
    @Email
    @UniqueEmailConstraint(message = "EMAIL_ALREADY_EXISTS")
    String email;

    @Size(min = 3, message = "INVALID_USERNAME")
    @UniqueUsernameConstraint(message = "USERNAME_ALREADY_EXISTS")
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    String firstName;

    String lastName;

    @DobConstraint(min = 14, message = "INVALID_DOB")
    LocalDate dob;
}

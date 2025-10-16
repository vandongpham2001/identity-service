package com.dongpv.sns.identity.dto.request.admin.user;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

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
    String id;

    @Email
    String email;

    @Size(min = 3, message = "INVALID_USERNAME")
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    String firstName;

    String lastName;

    @DobConstraint(min = 14, message = "INVALID_DOB")
    LocalDate dob;

    List<String> roles;
}

package com.dongpv.sns.identity.dto.request.auth;

import com.dongpv.sns.identity.validator.DobConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequestDto {
    @NotEmpty
    String email;

    @NotEmpty
    String password;

    @DobConstraint(min = 14, message = "INVALID_DOB")
    LocalDate dob;
}

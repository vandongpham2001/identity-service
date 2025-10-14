package com.dongpv.sns.identity.dto.request.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
}

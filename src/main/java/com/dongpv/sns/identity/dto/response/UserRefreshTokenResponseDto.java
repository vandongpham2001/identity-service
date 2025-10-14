package com.dongpv.sns.identity.dto.response;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRefreshTokenResponseDto {
    String id;
    String token;
    Instant expiryDate;
    String email;
}

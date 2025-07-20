package com.dongpv.sns.identity.dto.response;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponseDto {
    String accessToken;
    String refreshToken;
    Date expiredAt;
    boolean isAuthenticated;
}

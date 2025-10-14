package com.dongpv.sns.identity.dto.request.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRefreshTokenRequestDto {
    String userId;

    String email;

    String deviceInfo;
}

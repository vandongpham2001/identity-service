package com.dongpv.sns.identity.dto.response;

import java.util.Set;

import com.dongpv.sns.identity.code.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
    String id;

    String email;

    String username;

    Gender gender;

    Set<RoleResponseDto> roles;
}

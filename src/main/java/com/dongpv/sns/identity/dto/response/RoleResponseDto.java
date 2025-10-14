package com.dongpv.sns.identity.dto.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponseDto {
    String name;
    String description;
    Set<PermissionResponseDto> permissions;
}

package com.dongpv.sns.identity.dto.request.admin.role;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoleRequestDto {
    String name;
    String description;
    Set<String> permissions;
}

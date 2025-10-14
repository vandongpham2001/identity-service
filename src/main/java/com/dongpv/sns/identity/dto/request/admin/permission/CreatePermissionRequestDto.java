package com.dongpv.sns.identity.dto.request.admin.permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePermissionRequestDto {
    String name;
    String description;
}

package com.dongpv.sns.identity.controller.v1;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.request.admin.role.CreateRoleRequestDto;
import com.dongpv.sns.identity.dto.response.RoleResponseDto;
import com.dongpv.sns.identity.service.impl.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping
    ApiResponse<Page<RoleResponseDto>> getAll() {
        return ApiResponse.ok(roleService.filter());
    }

    @PostMapping
    ApiResponse<RoleResponseDto> create(@RequestBody CreateRoleRequestDto request) {
        return ApiResponse.ok(roleService.create(request));
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }
}

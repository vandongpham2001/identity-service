package com.dongpv.sns.identity.controller.v1;

import com.dongpv.sns.identity.dto.PageApiResponseDto;
import com.dongpv.sns.identity.service.RoleService;
import com.dongpv.sns.identity.util.FilterUtils;
import com.dongpv.sns.identity.util.PaginationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.request.admin.role.CreateRoleRequestDto;
import com.dongpv.sns.identity.dto.response.RoleResponseDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ApiResponse<PageApiResponseDto<RoleResponseDto>> filter(@RequestParam(required = false) final Map<String, String> requestParams) {
        PageRequest pageRequest = PaginationUtils.generatePageRequest(requestParams);
        var filter = FilterUtils.handleFilterRequest(requestParams, false);
        return ApiResponse.ok(PaginationUtils.buildPageRes(roleService.filter(pageRequest, filter)));
    }

    @GetMapping("/{roleId}")
    public ApiResponse<RoleResponseDto> getById(@PathVariable String roleId) {
        return ApiResponse.ok(roleService.findOneById(roleId));
    }

    @PostMapping
    public ApiResponse<RoleResponseDto> create(@RequestBody CreateRoleRequestDto request) {
        return ApiResponse.ok(roleService.create(request));
    }

    @PutMapping("/{roleId}")
    public ApiResponse<RoleResponseDto> update(@PathVariable String roleId, @RequestBody CreateRoleRequestDto request) {
        return ApiResponse.ok(roleService.update(roleId, request));
    }

    @DeleteMapping("/{roleId}")
    public ApiResponse<Void> delete(@PathVariable String roleId) {
        roleService.delete(roleId);
        return ApiResponse.<Void>builder().build();
    }
}

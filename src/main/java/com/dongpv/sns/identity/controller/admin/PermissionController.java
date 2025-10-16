package com.dongpv.sns.identity.controller.admin;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.identity.constant.RouteConstant;
import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.PageApiResponseDto;
import com.dongpv.sns.identity.dto.request.admin.permission.CreatePermissionRequestDto;
import com.dongpv.sns.identity.dto.request.admin.permission.UpdatePermissionRequestDto;
import com.dongpv.sns.identity.dto.response.PermissionResponseDto;
import com.dongpv.sns.identity.service.PermissionService;
import com.dongpv.sns.identity.util.FilterUtils;
import com.dongpv.sns.identity.util.PaginationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(RouteConstant.Admin.PERMISSION)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ApiResponse<PageApiResponseDto<PermissionResponseDto>> filter(
            @RequestParam(required = false) final Map<String, String> requestParams) {
        PageRequest pageRequest = PaginationUtils.generatePageRequest(requestParams);
        var filter = FilterUtils.handleFilterRequest(requestParams, false);
        return ApiResponse.ok(PaginationUtils.buildPageRes(permissionService.filter(pageRequest, filter)));
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionResponseDto> getById(@PathVariable String id) {
        return ApiResponse.ok(permissionService.findOneById(id));
    }

    @PostMapping
    public ApiResponse<PermissionResponseDto> create(@RequestBody CreatePermissionRequestDto request) {
        return ApiResponse.ok(permissionService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PermissionResponseDto> update(
            @PathVariable String id, @RequestBody UpdatePermissionRequestDto request) {
        return ApiResponse.ok(permissionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        permissionService.delete(id);
        return ApiResponse.ok();
    }
}

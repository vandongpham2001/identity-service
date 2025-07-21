package com.dongpv.sns.identity.controller.v1;

import java.util.Map;

import com.dongpv.sns.identity.dto.PageApiResponseDto;
import com.dongpv.sns.identity.service.PermissionService;
import com.dongpv.sns.identity.util.FilterUtils;
import com.dongpv.sns.identity.util.PaginationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.request.admin.permission.CreatePermissionRequestDto;
import com.dongpv.sns.identity.dto.response.PermissionResponseDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ApiResponse<PageApiResponseDto<PermissionResponseDto>> filter(@RequestParam(required = false) final Map<String, String> requestParams) {
        PageRequest pageRequest = PaginationUtils.generatePageRequest(requestParams);
        var filter = FilterUtils.handleFilterRequest(requestParams, false);
        return ApiResponse.ok(PaginationUtils.buildPageRes(permissionService.filter(pageRequest, filter)));
    }

    @GetMapping("/{permissionId}")
    public ApiResponse<PermissionResponseDto> getById(@PathVariable String permissionId) {
        return ApiResponse.ok(permissionService.findOneById(permissionId));
    }

    @PostMapping
    public ApiResponse<PermissionResponseDto> create(@RequestBody CreatePermissionRequestDto request) {
        return ApiResponse.ok(permissionService.create(request));
    }

    @PutMapping("/{permissionId}")
    public ApiResponse<PermissionResponseDto> update(@PathVariable String permissionId, @RequestBody CreatePermissionRequestDto request) {
        return ApiResponse.ok(permissionService.update(permissionId, request));
    }

    @DeleteMapping("/{permissionId}")
    public ApiResponse<Void> delete(@PathVariable String permissionId) {
        permissionService.delete(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}

package com.dongpv.sns.identity.controller.admin;

import com.dongpv.sns.identity.constant.RouteConstant;
import com.dongpv.sns.identity.dto.PageApiResponseDto;
import com.dongpv.sns.identity.service.RoleService;
import com.dongpv.sns.identity.util.FilterUtils;
import com.dongpv.sns.identity.util.PaginationUtils;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(RouteConstant.Admin.ROLE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ApiResponse<PageApiResponseDto<RoleResponseDto>> filter(@RequestParam(required = false) final Map<String, String> requestParams) {
        PageRequest pageRequest = PaginationUtils.generatePageRequest(requestParams);
        var filter = FilterUtils.handleFilterRequest(requestParams, false);
        return ApiResponse.ok(PaginationUtils.buildPageRes(roleService.filter(pageRequest, filter)));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponseDto> getById(@PathVariable String id) {
        return ApiResponse.ok(roleService.findOneById(id));
    }

    @PostMapping
    public ApiResponse<RoleResponseDto> save(@RequestBody CreateRoleRequestDto request) {
        return ApiResponse.ok(roleService.save(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        roleService.delete(id);
        return ApiResponse.ok();
    }
}

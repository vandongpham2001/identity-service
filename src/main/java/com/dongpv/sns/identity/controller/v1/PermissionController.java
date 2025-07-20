package com.dongpv.sns.identity.controller.v1;

import java.util.Map;

import com.dongpv.sns.identity.dto.PageApiResponseDto;
import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.util.PaginationUtils;
import com.dongpv.sns.identity.util.StringUtils;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.request.admin.permission.CreatePermissionRequestDto;
import com.dongpv.sns.identity.dto.response.PermissionResponseDto;
import com.dongpv.sns.identity.service.impl.PermissionService;

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
    ResponseEntity<ApiResponse<PageApiResponseDto<PermissionResponseDto>>> filter(@RequestParam(required = false) final Map<String, String> requestParams) {
        PageRequest pageRequest = PaginationUtils.generatePageRequest(requestParams);

        var filter = new BaseFilterRequestDto();
        filter.setKeyword(StringUtils.getValue(requestParams.get("keyword")));

        if (!StringUtils.getValue(requestParams.get("sortColumn")).isEmpty()) {
            filter.setSortColumn(
                    StringUtils.camelToSnake(StringUtils.getValue(requestParams.get("sortColumn"))));
        }

        if (!StringUtils.getValue(requestParams.get("sortType")).isEmpty()) {
            filter.setSortType(StringUtils.getValue(requestParams.get("sortType")));
        }
        return ResponseEntity.ok(ApiResponse.ok(PaginationUtils.buildPageRes(permissionService.filter(pageRequest, filter))));
    }

    @GetMapping("/{permissionId}")
    ResponseEntity<ApiResponse<PermissionResponseDto>> getById(@PathVariable @NotEmpty String permissionId) {
        return ResponseEntity.ok(ApiResponse.ok(permissionService.findOneById(permissionId)));
    }

    @PostMapping
    ResponseEntity<ApiResponse<PermissionResponseDto>> create(@RequestBody CreatePermissionRequestDto request) {
        return ResponseEntity.ok(ApiResponse.ok(permissionService.create(request)));
    }

    @PutMapping("/{permissionId}")
    ResponseEntity<ApiResponse<PermissionResponseDto>> update(@PathVariable String permissionId, @RequestBody CreatePermissionRequestDto request) {
        return ResponseEntity.ok(ApiResponse.ok(permissionService.update(permissionId, request)));
    }

    @DeleteMapping("/{permissionId}")
    ResponseEntity<ApiResponse<Void>> delete(@PathVariable String permissionId) {
        permissionService.delete(permissionId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }
}

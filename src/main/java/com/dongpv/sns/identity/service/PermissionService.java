package com.dongpv.sns.identity.service;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.dto.request.admin.permission.CreatePermissionRequestDto;
import com.dongpv.sns.identity.dto.request.admin.permission.UpdatePermissionRequestDto;
import com.dongpv.sns.identity.dto.response.PermissionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PermissionService {
    PermissionResponseDto create(CreatePermissionRequestDto request);
    PermissionResponseDto update(String id, UpdatePermissionRequestDto request);
    void delete(String id);
    Page<PermissionResponseDto> filter(PageRequest pageRequest, BaseFilterRequestDto filter);
    PermissionResponseDto findOneById(String id);
}

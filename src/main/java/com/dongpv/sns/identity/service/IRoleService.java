package com.dongpv.sns.identity.service;

import com.dongpv.sns.identity.dto.request.admin.role.CreateRoleRequestDto;
import com.dongpv.sns.identity.dto.response.RoleResponseDto;
import org.springframework.data.domain.Page;

public interface IRoleService {
    RoleResponseDto create(CreateRoleRequestDto request);
    RoleResponseDto update(CreateRoleRequestDto request);
    void delete(String role);
    Page<RoleResponseDto> filter();
    RoleResponseDto findOneById(String id);
}

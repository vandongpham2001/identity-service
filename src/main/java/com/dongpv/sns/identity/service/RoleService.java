package com.dongpv.sns.identity.service;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.dto.request.admin.role.CreateRoleRequestDto;
import com.dongpv.sns.identity.dto.response.RoleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RoleService {
    RoleResponseDto save(CreateRoleRequestDto request);
    void delete(String id);
    Page<RoleResponseDto> filter(PageRequest pageRequest, BaseFilterRequestDto filter);
    RoleResponseDto findOneById(String id);
}

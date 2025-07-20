package com.dongpv.sns.identity.service.impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.dto.request.admin.role.CreateRoleRequestDto;
import com.dongpv.sns.identity.dto.response.RoleResponseDto;
import com.dongpv.sns.identity.mapper.RoleMapper;
import com.dongpv.sns.identity.repository.PermissionRepository;
import com.dongpv.sns.identity.repository.RoleRepository;
import com.dongpv.sns.identity.service.IRoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Override
    public RoleResponseDto create(CreateRoleRequestDto request) {
        var roleOpt = roleRepository.findByName(request.getName());
        var role = roleOpt.map(existRole -> {
            RoleMapper.INSTANCE.toUpdateEntity(existRole, request);
            return existRole;
        }).orElseGet(() -> RoleMapper.INSTANCE.toCreateEntity(request));

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return RoleMapper.INSTANCE.toResponseDto(role);

    }

    @Override
    public RoleResponseDto update(CreateRoleRequestDto request) {
        return null;
    }

    @Override
    public void delete(String role) {
        roleRepository.deleteById(role);
    }

    @Override
    public Page<RoleResponseDto> filter() {
        return null;
    }

    @Override
    public RoleResponseDto findOneById(String id) {
        return null;
    }
}

package com.dongpv.sns.identity.service.impl;

import java.util.HashSet;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.entity.RoleEntity;
import com.dongpv.sns.identity.exception.DataNotFoundException;
import com.dongpv.sns.identity.service.RoleService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.dto.request.admin.role.CreateRoleRequestDto;
import com.dongpv.sns.identity.dto.response.RoleResponseDto;
import com.dongpv.sns.identity.mapper.RoleMapper;
import com.dongpv.sns.identity.repository.PermissionRepository;
import com.dongpv.sns.identity.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static com.dongpv.sns.identity.util.PaginationUtils.ASC;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Override
    public RoleResponseDto create(CreateRoleRequestDto request) {
        var entityOpt = roleRepository.findByName(request.getName());
        var entity = entityOpt.map(existRole -> {
            RoleMapper.INSTANCE.toUpdateEntity(existRole, request);
            return existRole;
        }).orElseGet(() -> RoleMapper.INSTANCE.toCreateEntity(request));

        var permissions = permissionRepository.findAllById(request.getPermissions());
        entity.setPermissions(new HashSet<>(permissions));
        entity = roleRepository.save(entity);
        return RoleMapper.INSTANCE.toResponseDto(entity);

    }

    @Override
    public void delete(String id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Page<RoleResponseDto> filter(PageRequest pageRequest, BaseFilterRequestDto filter) {
        Sort sortable;
        if (filter.getSortType().equals(ASC)) {
            sortable = Sort.by(filter.getSortColumn()).ascending();
        } else {
            sortable = Sort.by(filter.getSortColumn()).descending();
        }
        Pageable paging = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortable);
        Page<RoleEntity> entities = roleRepository.filter(filter.getKeyword(), paging);
        Page<RoleResponseDto> data = entities.map(RoleMapper.INSTANCE::toResponseDto);
        return new PageImpl<>(data.getContent(), pageRequest, entities.getTotalElements());
    }

    @Override
    public RoleResponseDto findOneById(String id) {
        RoleEntity entity = roleRepository.findById(id).orElseThrow(DataNotFoundException::new);
        return RoleMapper.INSTANCE.toResponseDto(entity);
    }
}

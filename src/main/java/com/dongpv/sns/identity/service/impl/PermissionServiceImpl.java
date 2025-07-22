package com.dongpv.sns.identity.service.impl;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.exception.DataNotFoundException;
import com.dongpv.sns.identity.service.PermissionService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.dto.request.admin.permission.CreatePermissionRequestDto;
import com.dongpv.sns.identity.dto.response.PermissionResponseDto;
import com.dongpv.sns.identity.entity.PermissionEntity;
import com.dongpv.sns.identity.mapper.PermissionMapper;
import com.dongpv.sns.identity.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static com.dongpv.sns.identity.util.PaginationUtils.ASC;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;

    @Override
    public PermissionResponseDto create(CreatePermissionRequestDto request) {
        PermissionEntity entity = PermissionMapper.INSTANCE.toCreateEntity(request);
        entity = permissionRepository.save(entity);
        return PermissionMapper.INSTANCE.toResponseDto(entity);
    }

    @Override
    public PermissionResponseDto update(String id, CreatePermissionRequestDto request) {
        PermissionEntity entity = permissionRepository.findById(id).orElseThrow(DataNotFoundException::new);
        PermissionMapper.INSTANCE.toUpdateEntity(entity, request);
        entity = permissionRepository.save(entity);
        return PermissionMapper.INSTANCE.toResponseDto(entity);
    }

    @Override
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }

    @Override
    public Page<PermissionResponseDto> filter(PageRequest pageRequest, BaseFilterRequestDto filter) {
        Sort sortable;
        if (filter.getSortType().equals(ASC)) {
            sortable = Sort.by(filter.getSortColumn()).ascending();
        } else {
            sortable = Sort.by(filter.getSortColumn()).descending();
        }
        Pageable paging = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortable);
        Page<PermissionEntity> entities = permissionRepository.filter(filter.getKeyword(), paging);
        Page<PermissionResponseDto> data = entities.map(PermissionMapper.INSTANCE::toResponseDto);
        return new PageImpl<>(data.getContent(), pageRequest, entities.getTotalElements());
    }

    @Override
    public PermissionResponseDto findOneById(String id) {
        PermissionEntity entity = permissionRepository.findById(id).orElseThrow(DataNotFoundException::new);
        return PermissionMapper.INSTANCE.toResponseDto(entity);
    }
}

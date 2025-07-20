package com.dongpv.sns.identity.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dongpv.sns.identity.dto.request.admin.permission.CreatePermissionRequestDto;
import com.dongpv.sns.identity.dto.response.PermissionResponseDto;
import com.dongpv.sns.identity.entity.PermissionEntity;

@Mapper(unmappedTargetPolicy = IGNORE, nullValueCheckStrategy = ALWAYS)
public interface PermissionMapper
        extends GenericMapper<
                PermissionEntity, CreatePermissionRequestDto, CreatePermissionRequestDto, PermissionResponseDto> {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
}

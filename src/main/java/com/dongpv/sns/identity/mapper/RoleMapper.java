package com.dongpv.sns.identity.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.dongpv.sns.identity.dto.request.admin.role.CreateRoleRequestDto;
import com.dongpv.sns.identity.dto.response.RoleResponseDto;
import com.dongpv.sns.identity.entity.RoleEntity;

@Mapper(unmappedTargetPolicy = IGNORE, nullValueCheckStrategy = ALWAYS)
public interface RoleMapper
        extends GenericMapper<RoleEntity, CreateRoleRequestDto, CreateRoleRequestDto, RoleResponseDto> {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Override
    @Mapping(target = "permissions", ignore = true)
    RoleEntity toCreateEntity(CreateRoleRequestDto request);

    @Override
    @Mapping(target = "permissions", ignore = true)
    void toUpdateEntity(@MappingTarget RoleEntity entity, CreateRoleRequestDto request);
}

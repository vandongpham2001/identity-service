package com.dongpv.sns.identity.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.dongpv.sns.identity.dto.request.admin.user.CreateUserRequestDto;
import com.dongpv.sns.identity.dto.request.admin.user.UpdateUserRequestDto;
import com.dongpv.sns.identity.dto.response.UserResponseDto;
import com.dongpv.sns.identity.entity.UserEntity;

@Mapper(unmappedTargetPolicy = IGNORE, nullValueCheckStrategy = ALWAYS)
public interface UserMapper
        extends GenericMapper<UserEntity, CreateUserRequestDto, UpdateUserRequestDto, UserResponseDto> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    @Mapping(target = "roles", ignore = true)
    UserEntity toCreateEntity(CreateUserRequestDto request);

    @Override
    @Mapping(target = "roles", ignore = true)
    void toUpdateEntity(@MappingTarget UserEntity entity, UpdateUserRequestDto request);
}

package com.dongpv.sns.identity.mapper;

import org.mapstruct.MappingTarget;

public interface GenericMapper<E, C, U, D> {
    D toResponseDto(E entity);

    E toCreateEntity(C request);

    void toUpdateEntity(@MappingTarget E entity, U request);
}

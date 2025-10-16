package com.dongpv.sns.identity.service.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dongpv.sns.identity.exception.DataNotFoundException;
import com.dongpv.sns.identity.mapper.GenericMapper;
import com.dongpv.sns.identity.service.GenericService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class GenericServiceImpl<E, I, C, U, D> implements GenericService<I, C, U, D> {

    protected final JpaRepository<E, I> repository;
    protected final GenericMapper<E, C, U, D> mapper;

    @Override
    public D create(C request) {
        var entity = mapper.toCreateEntity(request);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    @Override
    public D update(I id, U request) {
        var entity = repository.findById(id).orElseThrow(DataNotFoundException::new);
        mapper.toUpdateEntity(entity, request);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    @Override
    public void delete(I[] ids) {
        for (I id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public D getById(I id) {
        var entity = repository.findById(id).orElseThrow(DataNotFoundException::new);
        return mapper.toResponseDto(entity);
    }

    @Override
    public List<D> getAll() {
        var entities = repository.findAll();
        return entities.stream().map(mapper::toResponseDto).toList();
    }
}

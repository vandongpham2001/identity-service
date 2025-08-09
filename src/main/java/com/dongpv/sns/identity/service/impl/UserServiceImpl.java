package com.dongpv.sns.identity.service.impl;

import java.util.HashSet;
import java.util.List;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.exception.UserExistException;
import com.dongpv.sns.identity.exception.UserNotFoundException;
import com.dongpv.sns.identity.service.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dongpv.sns.identity.constant.PredefinedRole;
import com.dongpv.sns.identity.dto.request.admin.user.CreateUserRequestDto;
import com.dongpv.sns.identity.dto.request.admin.user.UpdateUserRequestDto;
import com.dongpv.sns.identity.dto.response.UserResponseDto;
import com.dongpv.sns.identity.entity.RoleEntity;
import com.dongpv.sns.identity.entity.UserEntity;
import com.dongpv.sns.identity.mapper.UserMapper;
import com.dongpv.sns.identity.repository.RoleRepository;
import com.dongpv.sns.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    EntityManager entityManager;
    PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto create(CreateUserRequestDto request) {
        UserEntity entity = UserMapper.INSTANCE.toCreateEntity(request);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<RoleEntity> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        entity.setRoles(roles);
        try {
            entity = userRepository.save(entity);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistException();
        }
        return UserMapper.INSTANCE.toResponseDto(entity);
    }

    @Override
    public UserResponseDto update(String id, UpdateUserRequestDto request) {
        UserEntity entity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        UserMapper.INSTANCE.toUpdateEntity(entity, request);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        entity.setRoles(new HashSet<>(roles));

        return UserMapper.INSTANCE.toResponseDto(userRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        var entity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        entity.softDelete();
        userRepository.save(entity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDto> filter(PageRequest pageRequest, BaseFilterRequestDto filter) {
        String commonQuery =
                """
                    WHERE
                      u.is_deleted = 0
                    AND (
                      COALESCE(:keyword, '') = ''
                      OR LOWER(u.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
                      OR LOWER(u.username) LIKE LOWER(CONCAT('%',:keyword,'%'))
                    )
                """;

        String countQuery =
                """
                    SELECT
                      count(u.id)
                    FROM users u
                """
                        + commonQuery;

        var countQueryResult =
                entityManager
                        .createNativeQuery(countQuery, Integer.class)
                        .setParameter("keyword", filter.getKeyword());
        Integer total = (Integer) countQueryResult.getSingleResult();
        String getQuery =
                """
                    SELECT u.id
                    , u.email
                    , u.username
                    , u.email_verified
                    , u.password
                    , u.created_at
                    , u.updated_at
                    , u.created_by
                    , u.updated_by
                    , u.deleted_at
                    , u.is_deleted
                    , u.deleted_by
                    FROM users u
                """
                        + commonQuery
                        + " ORDER BY "
                        + filter.getSortColumn()
                        + " "
                        + filter.getSortType()
                        + " OFFSET :offset LIMIT :limit";
        var getQueryResult =
                entityManager
                        .createNativeQuery(getQuery, UserEntity.class)
                        .setParameter("keyword", filter.getKeyword())
                        .setParameter("offset", pageRequest.getOffset())
                        .setParameter("limit", pageRequest.getPageSize());
        @SuppressWarnings("unchecked")
        List<UserEntity> entities = getQueryResult.getResultList();
        var data = entities.stream().map(UserMapper.INSTANCE::toResponseDto).toList();
        return new PageImpl<>(data, pageRequest, total);
    }

    @Override
    public UserResponseDto findOneById(String id) {
        return UserMapper.INSTANCE.toResponseDto(
                userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserResponseDto me() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        UserEntity entity =
                userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        return UserMapper.INSTANCE.toResponseDto(entity);
    }
}

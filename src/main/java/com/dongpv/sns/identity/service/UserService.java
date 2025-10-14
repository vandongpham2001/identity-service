package com.dongpv.sns.identity.service;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.dto.request.admin.user.CreateUserRequestDto;
import com.dongpv.sns.identity.dto.request.admin.user.UpdateUserRequestDto;
import com.dongpv.sns.identity.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserService {
    UserResponseDto create(CreateUserRequestDto request);
    UserResponseDto update(String id, UpdateUserRequestDto request);
    void delete(String id);
    Page<UserResponseDto> filter(PageRequest pageRequest, BaseFilterRequestDto filter);
    UserResponseDto findOneById(String id);
    UserResponseDto me();
}

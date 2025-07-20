package com.dongpv.sns.identity.service;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.dto.request.admin.user.CreateUserRequestDto;
import com.dongpv.sns.identity.dto.request.admin.user.UpdateUserRequestDto;
import com.dongpv.sns.identity.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IUserService {
    UserResponseDto create(CreateUserRequestDto request);
    UserResponseDto update(String userId, UpdateUserRequestDto request);
    void delete(String userId);
    Page<UserResponseDto> filter(PageRequest pageRequest, BaseFilterRequestDto filter);
    UserResponseDto findOneById(String id);
    UserResponseDto getMyInfo();
}

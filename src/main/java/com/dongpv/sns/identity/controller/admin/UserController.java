package com.dongpv.sns.identity.controller.admin;

import java.util.Map;

import com.dongpv.sns.identity.constant.RouteConstant;
import com.dongpv.sns.identity.dto.PageApiResponseDto;
import com.dongpv.sns.identity.service.UserService;
import com.dongpv.sns.identity.util.FilterUtils;
import com.dongpv.sns.identity.util.PaginationUtils;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.request.admin.user.CreateUserRequestDto;
import com.dongpv.sns.identity.dto.request.admin.user.UpdateUserRequestDto;
import com.dongpv.sns.identity.dto.response.UserResponseDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(RouteConstant.Admin.USERS)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping
    public ApiResponse<PageApiResponseDto<UserResponseDto>> filter(@RequestParam(required = false) final Map<String, String> requestParams) {
        PageRequest pageRequest = PaginationUtils.generatePageRequest(requestParams);
        var filter = FilterUtils.handleFilterRequest(requestParams, true);
        Page<UserResponseDto> pageResponseDto = userService.filter(pageRequest, filter);
        return ApiResponse.ok(PaginationUtils.buildPageRes(pageResponseDto));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponseDto> getById(@PathVariable("userId") String userId) {
        return ApiResponse.ok(userService.findOneById(userId));
    }

    @PostMapping
    public ApiResponse<UserResponseDto> create(@RequestBody @Valid CreateUserRequestDto request) {
        return ApiResponse.ok(userService.create(request));
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponseDto> update(@PathVariable String userId, @RequestBody UpdateUserRequestDto request) {
        return ApiResponse.ok(userService.update(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> delete(@PathVariable String userId) {
        userService.delete(userId);
        return ApiResponse.ok();
    }
}

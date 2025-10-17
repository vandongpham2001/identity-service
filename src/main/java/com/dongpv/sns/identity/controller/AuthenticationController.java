package com.dongpv.sns.identity.controller;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.identity.constant.RouteConstant;
import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.request.auth.*;
import com.dongpv.sns.identity.dto.response.AuthenticationResponseDto;
import com.dongpv.sns.identity.dto.response.IntrospectResponseDto;
import com.dongpv.sns.identity.dto.response.RegisterResponseDto;
import com.dongpv.sns.identity.dto.response.UserResponseDto;
import com.dongpv.sns.identity.service.AuthenticationService;
import com.dongpv.sns.identity.service.UserService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(RouteConstant.AUTH)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponseDto> login(@Valid @RequestBody AuthenticationRequestDto request) {
        var result = authenticationService.login(request);
        return ApiResponse.ok(result);
    }

    @PostMapping("/register")
    public ApiResponse<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        var result = authenticationService.register(request);
        return ApiResponse.ok(result);
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponseDto> introspect(@Valid @RequestBody IntrospectRequestDto request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.ok(result);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody LogoutRequestDto request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.ok(result);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponseDto> me() {
        return ApiResponse.ok(userService.me());
    }
}

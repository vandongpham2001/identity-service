package com.dongpv.sns.identity.controller.v1;

import java.text.ParseException;

import com.dongpv.sns.identity.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.request.auth.AuthenticationRequestDto;
import com.dongpv.sns.identity.dto.request.auth.IntrospectRequestDto;
import com.dongpv.sns.identity.dto.request.auth.LogoutRequestDto;
import com.dongpv.sns.identity.dto.request.auth.RefreshTokenRequestDto;
import com.dongpv.sns.identity.dto.response.AuthenticationResponseDto;
import com.dongpv.sns.identity.dto.response.IntrospectResponseDto;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponseDto> login(@Valid @RequestBody AuthenticationRequestDto request) {
        var result = authenticationService.login(request);
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
}

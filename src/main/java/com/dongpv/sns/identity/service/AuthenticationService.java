package com.dongpv.sns.identity.service;

import java.text.ParseException;

import com.dongpv.sns.identity.dto.request.auth.*;
import com.dongpv.sns.identity.dto.response.AuthenticationResponseDto;
import com.dongpv.sns.identity.dto.response.IntrospectResponseDto;
import com.dongpv.sns.identity.dto.response.RegisterResponseDto;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    AuthenticationResponseDto login(AuthenticationRequestDto request);

    RegisterResponseDto register(RegisterRequestDto request);

    IntrospectResponseDto introspect(IntrospectRequestDto request) throws JOSEException, ParseException;

    void logout(LogoutRequestDto request) throws ParseException, JOSEException;

    AuthenticationResponseDto refreshToken(RefreshTokenRequestDto request);
}

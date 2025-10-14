package com.dongpv.sns.identity.service;

import java.text.ParseException;

import com.dongpv.sns.identity.dto.request.auth.AuthenticationRequestDto;
import com.dongpv.sns.identity.dto.request.auth.IntrospectRequestDto;
import com.dongpv.sns.identity.dto.request.auth.LogoutRequestDto;
import com.dongpv.sns.identity.dto.request.auth.RefreshTokenRequestDto;
import com.dongpv.sns.identity.dto.response.AuthenticationResponseDto;
import com.dongpv.sns.identity.dto.response.IntrospectResponseDto;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    AuthenticationResponseDto login(AuthenticationRequestDto request);

    IntrospectResponseDto introspect(IntrospectRequestDto request) throws JOSEException, ParseException;

    void logout(LogoutRequestDto request) throws ParseException, JOSEException;

    AuthenticationResponseDto refreshToken(RefreshTokenRequestDto request);
}

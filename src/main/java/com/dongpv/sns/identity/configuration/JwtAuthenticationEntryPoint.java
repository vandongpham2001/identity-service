package com.dongpv.sns.identity.configuration;

import java.io.IOException;

import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_EXCEPTION;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final MultiRecordErrorResponseDtoBase responseDto =
                new MultiRecordErrorResponseDtoBase(
                        HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        responseDto.addDetail(KEY_EXCEPTION, authException.getLocalizedMessage());
        String responseMsg = objectMapper.writeValueAsString(responseDto);
        response.getWriter().write(responseMsg);
        response.flushBuffer();
    }
}

package com.dongpv.sns.identity.configuration;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_MESSAGE;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dongpv.sns.identity.code.ErrorCode;
import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;
import com.dongpv.sns.identity.exception.ApiResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GatewayFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ApiResourceNotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            MultiRecordErrorResponseDtoBase errorResponse =
                    new MultiRecordErrorResponseDtoBase(ErrorCode.API_RESOURCE_NOT_FOUND.getCode(), e.getMessage());
            errorResponse.addFirstRecordDetail(KEY_MESSAGE, e.getLocalizedMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            String responseMsg = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(responseMsg);
            response.flushBuffer();
        }
    }
}

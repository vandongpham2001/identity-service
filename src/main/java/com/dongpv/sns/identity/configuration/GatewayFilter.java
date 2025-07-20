package com.dongpv.sns.identity.configuration;

import com.dongpv.sns.identity.dto.MultiRecordErrorResponseDtoBase;
import com.dongpv.sns.identity.exception.ApiResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dongpv.sns.identity.constant.CommonConstant.KEY_MESSAGE;

@Component
public class GatewayFilter extends OncePerRequestFilter {
    @Value("${gateway.trusted-header}")
    private String trustedHeader;

    @Value("${gateway.token}")
    private String gatewayToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String gatewayHeader = request.getHeader(trustedHeader);
            if (gatewayHeader == null || !gatewayHeader.equals(gatewayToken)) {
                throw new ApiResourceNotFoundException();
            }

            filterChain.doFilter(request, response);
        } catch (ApiResourceNotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            MultiRecordErrorResponseDtoBase errorResponse = new MultiRecordErrorResponseDtoBase(
                    HttpStatus.NOT_FOUND.value(), e.getMessage()
            );
            errorResponse.addFirstRecordDetail(KEY_MESSAGE, e.getLocalizedMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            String responseMsg = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(responseMsg);
            response.flushBuffer();
        }
    }
}

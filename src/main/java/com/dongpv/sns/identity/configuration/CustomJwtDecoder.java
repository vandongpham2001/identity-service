package com.dongpv.sns.identity.configuration;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Objects;

import com.dongpv.sns.identity.code.ErrorCode;
import com.dongpv.sns.identity.exception.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.dongpv.sns.identity.security.JwtTokenPublicUtils;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtDecoder implements JwtDecoder {
    @NonFinal
    @Value("${jwt.public-key}")
    protected String jwtPublicKeyEncode;

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder = null;

    JwtTokenPublicUtils jwtTokenPublicUtils;

    @Override
    public Jwt decode(String token) {
        try {
            jwtTokenPublicUtils.verifyToken(token);
        } catch (JOSEException | ParseException e) {
            throw new CommonException(ErrorCode.INVALID_TOKEN);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            var publicKey = JwtTokenPublicUtils.getPublicKey(jwtPublicKeyEncode);
            nimbusJwtDecoder =
                    NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey).build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}

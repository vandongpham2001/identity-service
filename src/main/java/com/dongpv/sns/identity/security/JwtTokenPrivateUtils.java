package com.dongpv.sns.identity.security;

import static com.dongpv.sns.identity.constant.CommonConstant.ROLE_PREFIX;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dongpv.sns.identity.entity.UserEntity;
import com.dongpv.sns.identity.exception.UnauthorizedUserException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenPrivateUtils {
    @NonFinal
    @Value("${jwt.private-key}")
    protected String jwtPrivateKeyEncode;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long jwtValidDuration;

    @NonFinal
    @Value("${jwt.issuer}")
    protected String issuer;

    @NonFinal
    @Value("${jwt.secret-key}")
    protected String jwtSecretKey;

    private static final String HMAC_ALGO = "HmacSHA512";

    public static PrivateKey getPrivateKey(String privateKeyEncode) {
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpecPKCS8 =
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyEncode));
            return kf.generatePrivate(keySpecPKCS8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("Can't init RSA key", e);
            throw new UnauthorizedUserException();
        }
    }

    public String generateToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(issuer)
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(jwtValidDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("id", user.getId())
                .claim("username", user.getUsername())
                .build();

        SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);

        try {
            signedJWT.sign(new RSASSASigner(getPrivateKey(jwtPrivateKeyEncode)));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            LOGGER.error("Can't create token", e);
            throw new UnauthorizedUserException();
        }
    }

    public String buildScope(UserEntity user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add(ROLE_PREFIX + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }

        return stringJoiner.toString();
    }

    public String generateUUIDToken() {
        return UUID.randomUUID().toString();
    }

    public String hashToken(String token) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecretKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
            mac.init(secretKeySpec);

            byte[] hmacBytes = mac.doFinal(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LOGGER.error("Can't generate refresh token", e);
            throw new UnauthorizedUserException();
        }
    }
}

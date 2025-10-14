package com.dongpv.sns.identity.security;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import com.dongpv.sns.identity.exception.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dongpv.sns.identity.exception.UnauthorizedUserException;
import com.dongpv.sns.identity.repository.InvalidatedTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenPublicUtils {
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.public-key}")
    protected String jwtPublicKeyEncode;

    public static PublicKey getPublicKey(String publicKeyEncode) {
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 =
                    new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyEncode));
            return kf.generatePublic(keySpecX509);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("Can't init RSA key", e);
            throw new UnauthorizedUserException();
        }
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) getPublicKey(jwtPublicKeyEncode));
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new UnauthenticatedException();
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new UnauthenticatedException();
        }

        return signedJWT;
    }

    public JWTClaimsSet getAllClaimsFromToken(String token) {
        try {
            SignedJWT signedJWT = verifyToken(token);
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            LOGGER.error("Token invalid", e);
            throw new UnauthenticatedException();
        }
    }

    public <T> T getClaimFromToken(String token, Function<JWTClaimsSet, T> claimsResolver) {
        JWTClaimsSet claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, JWTClaimsSet::getExpirationTime);
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, JWTClaimsSet::getSubject);
    }
}

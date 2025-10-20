package com.dongpv.sns.identity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.*;
import java.util.Base64;

import jakarta.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class IdentityServiceApplicationTests {

    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }

    @Test
    void generateKey() throws NoSuchAlgorithmException {
        KeyPair keyPair = generateRSAKeyPair();

        assertNotNull(keyPair, "Key pair should not be null");

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertNotNull(publicKey, "Public key should not be null");
        assertNotNull(privateKey, "Private key should not be null");

        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        System.out.println("PRIVATE KEY BASE64:");
        System.out.println(privateKeyBase64);
        System.out.println("\nPUBLIC KEY BASE64:");
        System.out.println(publicKeyBase64);
    }

    @Test
    void hash() throws NoSuchAlgorithmException {
        String password = "password";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String md5Hash = DatatypeConverter.printHexBinary(digest);
        LOGGER.info("MD5 round 1: {}", md5Hash);
        md.update(password.getBytes());
        digest = md.digest();
        md5Hash = DatatypeConverter.printHexBinary(digest);
        LOGGER.info("MD5 round 2: {}", md5Hash);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        LOGGER.info("BCrypt round 1: {}", passwordEncoder.encode(password));
        LOGGER.info("BCrypt round 2: {}", passwordEncoder.encode(password));
    }

    @Test
    void contextLoads() {
        // This test is intentionally left empty.
        // It verifies that the Spring application context loads without throwing any exceptions.
    }
}

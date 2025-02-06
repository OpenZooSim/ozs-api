package com.openzoosim.util;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.openzoosim.models.dtos.JWTOptions;

public class TokenUtil {
    
    private static final String JWT_ISS = "ozs-api";
    private static final String JWT_AUD = "openzoosim.com";
    private static final String JWT_SUB = "ozs-api__verification_token";  

    public static String generateVerificationToken(String secret, int userId) {
        Instant expiresAt = Instant.now().plusSeconds(60 * 60 * 1);
        JWTOptions options = new JWTOptions(secret, expiresAt, userId);
        return generateJWTWithOptions(options);
    }

    public static int verifyToken(String secret, String token) {
        JWTOptions options = new JWTOptions(secret, null, -1);
        return verifyJWTWithOptions(options, token);
    }

    public static int getTokenExpirationTimestamp(String secret, String token) {
        JWTOptions options = new JWTOptions(secret, null, -1);
        return getDecodedTokenExpirationTimestamp(options, token);
    }

    private static String generateJWTWithOptions(JWTOptions options) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(options.secret());
            Map<String, Object> payload = new HashMap<>();
            payload.put("user", options.userId());

            String token = JWT.create()
                .withIssuer(JWT_ISS)
                .withAudience(JWT_AUD)
                .withSubject(JWT_SUB)
                .withExpiresAt(options.expiresAt())
                .withPayload(payload)
                .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            return null;
        }
    }

    private static int verifyJWTWithOptions(JWTOptions options, String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(options.secret());
            DecodedJWT jwt = JWT.require(algorithm)
                .withIssuer(JWT_ISS)
                .withAudience(JWT_AUD)
                .withSubject(JWT_SUB)
                .build()
                .verify(token);

            String payloadJson = new String(StringUtil.base64Decode(jwt.getPayload()));
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payloadJson, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});

            return Integer.parseInt(payloadMap.get("user").toString());
        } catch (Exception exception) {
            return -1;
        }
    }

    private static int getDecodedTokenExpirationTimestamp(JWTOptions options, String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(options.secret());
            DecodedJWT jwt = JWT.require(algorithm)
                .withIssuer(JWT_ISS)
                .withAudience(JWT_AUD)
                .withSubject(JWT_SUB)
                .build()
                .verify(token);

            String payloadJson = new String(StringUtil.base64Decode(jwt.getPayload()));
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payloadJson, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});

            return Integer.parseInt(payloadMap.get("exp").toString());
        } catch (Exception exception) {
            return -1;
        }
    }    

}

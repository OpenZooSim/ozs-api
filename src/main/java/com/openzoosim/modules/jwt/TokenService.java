package com.openzoosim.modules.jwt;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.openzoosim.common.EnvService;
import com.openzoosim.common.RandomStringGenerator;
import com.openzoosim.modules.user.UserService;

@ApplicationScoped
public class TokenService {

    private final String VERIFICATION_TOKEN_SUB = "verification_token";
    private final String ACCESS_TOKEN_SUB = "access_token";
    private final String REFRESH_TOKEN_SUB = "refresh_token";

    private final String TOKEN_AUD = "https://openzoosim.com";
    private final String TOKEN_ISS = "https://api.openzoosim.com";

    @Inject
    JWTParser _parser;

    @Inject
    EnvService _envService;

    @Inject
    UserService userService;

    public String GenerateRandomString(int length) {
        return RandomStringGenerator.generateRandomString(length);
    }

    public String GenerateVerificationToken(String email) {
        return Jwt
                .upn(email)
                .audience(TOKEN_AUD)
                .issuer(TOKEN_ISS)
                .subject(VERIFICATION_TOKEN_SUB)
                .expiresIn(10800L)
                .signWithSecret(_envService.GetJWTTokenSecret());
    }

    public String GenerateAccessToken(String email) {
        return Jwt
                .upn(email)
                .audience(TOKEN_AUD)
                .issuer(TOKEN_ISS)
                .subject(ACCESS_TOKEN_SUB)
                .expiresIn(300L)
                .signWithSecret(_envService.GetJWTTokenSecret());
    }

    public String GenerateRefreshToken(String email) {
        return Jwt
                .upn(email)
                .claim("x-refresh", REFRESH_TOKEN_SUB)
                .audience(TOKEN_AUD)
                .issuer(TOKEN_ISS)
                .subject(REFRESH_TOKEN_SUB)
                .expiresIn(3600L * 24 * 7)
                .signWithSecret(_envService.GetJWTTokenSecret());
    }

    public String VerifyToken(String token) {
        try {
            JsonWebToken jwt = _parser.verify(token, _envService.GetJWTTokenSecret());
            return jwt.getName();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}

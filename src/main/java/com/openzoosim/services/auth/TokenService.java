package com.openzoosim.services.auth;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.Base64;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openzoosim.entities.UserEntity;
import com.openzoosim.models.dtos.IDTokenDTO;
import com.openzoosim.models.interfaces.IEnvService;
import com.openzoosim.models.interfaces.ITokenService;
import com.openzoosim.models.interfaces.IUserService;

@ApplicationScoped
public class TokenService implements ITokenService {

    private final String VERIFICATION_TOKEN_SUB = "ozs_verification_token";

    private final String TOKEN_AUD = "https://openzoosim.com";
    private final String TOKEN_ISS = "https://openzoosim.com/api";

    @Inject
    JWTParser _parser;

    @Inject
    Instance<IEnvService> _envService;

    @Inject
    Instance<IUserService> userService;

    public String generateVerificationToken(String email) {
        return Jwt
                .upn(email)
                .audience(TOKEN_AUD)
                .issuer(TOKEN_ISS)
                .subject(VERIFICATION_TOKEN_SUB)
                .expiresIn(_envService.get().GetVerificationExpiresInSecs())
                .signWithSecret(_envService.get().GetJWTTokenSecret());
    }

    public String generateIDToken(UserEntity entity) {
        IDTokenDTO idToken = new IDTokenDTO(entity);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(idToken);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        return Base64.getEncoder().encodeToString(jsonString.getBytes());
    }

    public String verifyToken(String token) {
        try {
            JsonWebToken jwt = _parser.verify(token, _envService.get().GetJWTTokenSecret());
            return jwt.getName();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public JsonWebToken getJWTData(String token) {
        try {
            JsonWebToken jwt = _parser.verify(token, _envService.get().GetJWTTokenSecret());
            return jwt;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }    

}

package com.openzoosim.modules.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.naming.AuthenticationException;

import com.openzoosim.common.CryptoService;
import com.openzoosim.modules.jwt.TokenService;
import com.openzoosim.modules.user.UserEntity;
import com.openzoosim.modules.user.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class AuthLoginService {

    @Inject
    TokenService _tokenService;

    @Inject
    UserService _userService;

    public AuthLoginResponseDTO Login(String authHeader) throws AuthenticationException {

        String[] credentials = new String(Base64.getDecoder().decode(authHeader.split(" ")[1]), StandardCharsets.UTF_8).split(":");

        UserEntity user = _userService.GetUserByEmail(credentials[0]);

        if(user == null || !user.IsVerified || user.IsArchived) {
            throw new AuthenticationException("Invalid username or password.");
        }

        boolean isPasswordVerified = CryptoService.VerifyHash(credentials[1], user.PasswordHash);

        if (!isPasswordVerified) {
            throw new AuthenticationException("Invalid username or password.");
        }

        String accessToken = _tokenService.GenerateAccessToken(user.Email);
        String refreshToken = _tokenService.GenerateRefreshToken(user.Email);

        AuthLoginResponseDTO response = new AuthLoginResponseDTO();
        response.access_token = accessToken;
        response.refresh_token = refreshToken;
        response.id_token = authHeader;
        return response;
    }

}

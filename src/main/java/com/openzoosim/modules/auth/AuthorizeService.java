package com.openzoosim.modules.auth;

import com.openzoosim.modules.jwt.TokenService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthorizeService {

    @Inject
    TokenService _tokenService;

    public boolean isAuthorized(String token) {
        String email = _tokenService.VerifyToken(token);
        return email != null && !email.isEmpty();
    }

}

package net.snowlynxsoftware.modules.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.snowlynxsoftware.modules.jwt.TokenService;

@ApplicationScoped
public class AuthorizeService {

    @Inject
    TokenService _tokenService;

    public boolean isAuthorized(String token) {
        String email = _tokenService.VerifyToken(token);
        return email != null && !email.isEmpty();
    }

}

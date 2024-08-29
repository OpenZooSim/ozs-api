package com.openzoosim.services.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import javax.naming.AuthenticationException;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.entities.UserLoginHistoryEntity;
import com.openzoosim.entities.UserSessionEntity;
import com.openzoosim.models.dtos.AuthLoginResponseDTO;
import com.openzoosim.models.interfaces.IAuthLoginService;
import com.openzoosim.models.interfaces.ICryptoService;
import com.openzoosim.models.interfaces.IEnvService;
import com.openzoosim.models.interfaces.ITokenService;
import com.openzoosim.models.interfaces.IUUIDService;
import com.openzoosim.models.interfaces.IUserService;
import com.openzoosim.repositories.UserLoginHistoryRepository;
import com.openzoosim.repositories.UserSessionRepository;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;

@ApplicationScoped
public class AuthLoginService implements IAuthLoginService {

    @Inject
    Instance<ITokenService> _tokenService;

    @Inject
    Instance<IUUIDService> _uuidService;    

    @Inject
    Instance<IEnvService> _envService; 

    @Inject
    Instance<IUserService> _userService;

    @Inject
    Instance<ICryptoService> _cryptoService;

    @Inject
    UserLoginHistoryRepository _userLoginHistoryRepository;

    @Inject
    UserSessionRepository _userSessionRepository;

    public AuthLoginResponseDTO login(String authHeader, String userAgent, String ip) throws AuthenticationException {

        String[] credentials = new String(Base64.getDecoder().decode(authHeader.split(" ")[1]), StandardCharsets.UTF_8).split(":");

        UserEntity user = _userService.get().getUserByEmail(credentials[0]);

        if(user == null || !user.IsVerified || user.IsArchived) {
            throw new AuthenticationException("Invalid username or password.");
        }

        boolean isPasswordVerified = _cryptoService.get().verifyHash(credentials[1], user.PasswordHash);

        if (!isPasswordVerified) {
            throw new AuthenticationException("Invalid username or password.");
        }

        String sessionToken = _uuidService.get().generateRandomToken();
        String encodedToken = Base64.getEncoder().encodeToString(sessionToken.getBytes());
        String idToken = _tokenService.get().generateIDToken(user);

        if (idToken == null) {
            throw new AuthenticationException("Invalid username or password.");
        }

        AuthLoginResponseDTO response = new AuthLoginResponseDTO();
        response.session_token = encodedToken;
        response.id_token = idToken;

        UserLoginHistoryEntity history = new UserLoginHistoryEntity();
        history.User = user;
        _userLoginHistoryRepository.persist(history);

        Long expiresAtLong = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + _envService.get().GetSessionTokenExpiresInSecs();

        LocalDateTime expiresAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(expiresAtLong), ZoneId.systemDefault());

        UserSessionEntity userSession = new UserSessionEntity();
        userSession.User = user;
        userSession.DeviceThumbprint = userAgent;
        userSession.IP = ip;
        userSession.SessionTokenID = sessionToken;
        userSession.ExpiresAt = expiresAt;
        _userSessionRepository.persist(userSession);

        return response;
    }

    public String getIDTokenForUserID(long userId) throws AuthenticationException {

        UserEntity user = _userService.get().getUserByID(userId);

        if(user == null || !user.IsVerified || user.IsArchived) {
            throw new AuthenticationException("Invalid username or password.");
        }

        return  _tokenService.get().generateIDToken(user);
    }
}

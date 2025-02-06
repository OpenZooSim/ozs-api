package com.openzoosim.services.impl;

import com.openzoosim.config.ApplicationConfig;
import com.openzoosim.entities.UserEntity;
import com.openzoosim.exceptions.CryptoException;
import com.openzoosim.exceptions.UserLoginException;
import com.openzoosim.models.dtos.UserCredentials;
import com.openzoosim.models.dtos.UserLoginResponse;
import com.openzoosim.repositories.UserLoginHistoryRepository;
import com.openzoosim.repositories.UserRepository;
import com.openzoosim.services.IAuthLoginService;
import com.openzoosim.services.ICryptoService;
import com.openzoosim.services.IUserSessionService;
import com.openzoosim.util.StringUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthLoginServiceImpl implements IAuthLoginService {

    @Inject
    UserRepository userRepository;

    @Inject
    ApplicationConfig config;

    @Inject
    Instance<IUserSessionService> userSessionService;

    @Inject
    Instance<ICryptoService> cryptoService;

    @Inject
    UserLoginHistoryRepository userLoginHistoryRepository;

    @Override
    public UserLoginResponse login(String authHeaderValue) throws UserLoginException {

        if (authHeaderValue == null || authHeaderValue.isEmpty() || !authHeaderValue.startsWith("Basic ")) {
            throw new UserLoginException();
        }

        String authHeaderSubstring = authHeaderValue.substring(6);

        String decodedHeader = StringUtil.base64Decode(authHeaderSubstring);

        UserCredentials userCredentials = StringUtil.parseBasicAuth(decodedHeader);

        if (StringUtil.isNullOrEmpty(userCredentials.email()) || StringUtil.isNullOrEmpty(userCredentials.password())) {
            throw new UserLoginException();
        }

        UserEntity user = this.userRepository.findByEmail(userCredentials.email().toLowerCase());

        if (user == null) {
            throw new UserLoginException(userCredentials.email().toLowerCase());
        }

        try {
            boolean result = this.cryptoService.get().verifyPassword(userCredentials.password(), user.password, this.config.getAppKey());
            if (!result) {
                throw new UserLoginException(userCredentials.email().toLowerCase());
            }
        } catch (CryptoException e) {
            throw new UserLoginException();
        }
        
        String sessionToken  = this.userSessionService.get().createUserSession(user);

        UserLoginResponse response = new UserLoginResponse(sessionToken);

        this.userLoginHistoryRepository.createLoginHistoryEntry(user);

        return response;
    }
    
}

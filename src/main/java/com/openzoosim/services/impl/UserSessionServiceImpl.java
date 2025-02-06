package com.openzoosim.services.impl;

import java.util.UUID;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.redis.RedisClient;
import com.openzoosim.redis.UserSessionPOJO;
import com.openzoosim.services.IUserSessionService;
import com.openzoosim.util.StringUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserSessionServiceImpl implements IUserSessionService {

    @Inject
    RedisClient redisClient;

    @Override
    public String createUserSession(UserEntity user) {
        UserSessionPOJO userSession = new UserSessionPOJO(user.id, user.email, user.role);
        String sessionId = StringUtil.base64Encode(UUID.randomUUID().toString());
        this.redisClient.setUserSession(sessionId, userSession);
        return sessionId;
    }

    @Override
    public UserSessionPOJO getUserSession(String sessionId) {
        return redisClient.getUserSession(sessionId);
    }
    
}

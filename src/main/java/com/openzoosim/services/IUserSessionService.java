package com.openzoosim.services;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.redis.UserSessionPOJO;

public interface IUserSessionService {
    String createUserSession(UserEntity user);
    UserSessionPOJO getUserSession(String sessionId);
}

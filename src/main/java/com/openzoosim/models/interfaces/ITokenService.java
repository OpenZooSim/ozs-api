package com.openzoosim.models.interfaces;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.openzoosim.entities.UserEntity;

public interface ITokenService {
    public String generateVerificationToken(String email);
    public String generateIDToken(UserEntity entity);
    public String verifyToken(String token);
    public JsonWebToken getJWTData(String token);
}

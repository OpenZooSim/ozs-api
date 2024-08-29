package com.openzoosim.models.interfaces;

import javax.naming.AuthenticationException;

import com.openzoosim.models.dtos.AuthLoginResponseDTO;

public interface IAuthLoginService {
    public AuthLoginResponseDTO login(String authHeader, String userAgent, String ip) throws AuthenticationException;;
    public String getIDTokenForUserID(long userId) throws AuthenticationException;;
}

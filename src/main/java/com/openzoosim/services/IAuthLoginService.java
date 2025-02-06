package com.openzoosim.services;

import com.openzoosim.exceptions.UserLoginException;
import com.openzoosim.models.dtos.UserLoginResponse;

public interface IAuthLoginService {
    UserLoginResponse login(String authHeaderValue) throws UserLoginException;
}

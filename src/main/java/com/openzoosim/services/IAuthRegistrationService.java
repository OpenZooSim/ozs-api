package com.openzoosim.services;

import com.openzoosim.exceptions.CryptoException;
import com.openzoosim.exceptions.NewUserNotUniqueException;
import com.openzoosim.models.dtos.UserRegistrationRequest;

public interface IAuthRegistrationService {

    boolean register(UserRegistrationRequest request) throws CryptoException, NewUserNotUniqueException;
    boolean verify(String token);

}

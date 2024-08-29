package com.openzoosim.models.interfaces;

import com.openzoosim.models.dtos.UserRegistrationRequestDTO;
import com.openzoosim.models.dtos.UserRegistrationResponseDTO;

public interface IUserRegistrationService {
    UserRegistrationResponseDTO registerNewUser(UserRegistrationRequestDTO dto);
    String verifyNewUser(String token);
}

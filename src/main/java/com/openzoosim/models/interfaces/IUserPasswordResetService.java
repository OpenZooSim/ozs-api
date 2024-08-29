package com.openzoosim.models.interfaces;

import com.openzoosim.models.dtos.UserPasswordResetRequestDTO;
import com.openzoosim.models.dtos.UserPasswordResetVerificationRequestDTO;

public interface IUserPasswordResetService {
    public boolean requestPasswordResetForUser(UserPasswordResetRequestDTO requestDTO);
    public boolean requestPasswordResetVerificationForUser(UserPasswordResetVerificationRequestDTO requestDTO);
}

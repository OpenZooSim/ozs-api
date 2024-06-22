package com.openzoosim.modules.auth;

import java.time.LocalDateTime;

import com.openzoosim.common.CryptoService;
import com.openzoosim.modules.email.EmailService;
import com.openzoosim.modules.jwt.TokenService;
import com.openzoosim.modules.user.UserEntity;
import com.openzoosim.modules.user.UserService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserPasswordResetService {
    
    private static final int PASSWORD_RESET_TOKEN_LENGTH = 16;

    private static final int HOURS_UNTIL_PASSWORD_RESET_TOKEN_EXPIRES = 3;

    @Inject
    TokenService _tokenService;

    @Inject
    UserService _userService;

    @Inject
    EmailService _emailService;    

    @Inject
    UserPasswordResetRepository _userPasswordResetRepository;

    public boolean RequestPasswordResetForUser(UserPasswordResetRequestDTO requestDTO) {

        UserEntity existingUser = _userService.GetUserByEmail(requestDTO.email);
        if (existingUser == null || existingUser.IsBanned) {
            return false;
        }

        String passwordResetToken = _tokenService.GenerateRandomString(PASSWORD_RESET_TOKEN_LENGTH);

        LocalDateTime expiresAt = LocalDateTime.now().plusHours(HOURS_UNTIL_PASSWORD_RESET_TOKEN_EXPIRES);

        UserPasswordResetEntity passwordResetUser = new UserPasswordResetEntity();
        passwordResetUser.ExpiresAt = expiresAt;
        passwordResetUser.User = existingUser;
        passwordResetUser.Token = CryptoService.HashString(passwordResetToken);

        _userPasswordResetRepository.persist(passwordResetUser);

        boolean isEmailSent = _emailService.SendEmail(existingUser.Email, "do-not-reply@openzoosim.com", "OpenZooSim - Reset Your Password", 
        "<h2>OpenZooSim - Reset Your Password</h2> " +
                        "<p>To reset your password, please click the link below!</p>" +
                        "<p><a href=\"http://localhost:3000/auth/password-reset-verify?token=" + passwordResetToken + "\">RESET PASSWORD</a></p>");

        return isEmailSent;

    }

}

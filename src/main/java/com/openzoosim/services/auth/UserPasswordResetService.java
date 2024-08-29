package com.openzoosim.services.auth;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.entities.UserPasswordResetEntity;
import com.openzoosim.models.dtos.UserPasswordResetRequestDTO;
import com.openzoosim.models.dtos.UserPasswordResetVerificationRequestDTO;
import com.openzoosim.models.interfaces.IAppLogService;
import com.openzoosim.models.interfaces.ICryptoService;
import com.openzoosim.models.interfaces.IEmailService;
import com.openzoosim.models.interfaces.ITokenService;
import com.openzoosim.models.interfaces.IUserPasswordResetService;
import com.openzoosim.models.interfaces.IUserService;
import com.openzoosim.repositories.UserPasswordResetRepository;
import com.openzoosim.services.email.templates.UserPasswordResetEmailTemplate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserPasswordResetService implements IUserPasswordResetService {
    
    private static final int PASSWORD_RESET_TOKEN_LENGTH = 16;

    private static final int HOURS_UNTIL_PASSWORD_RESET_TOKEN_EXPIRES = 3;

    @Inject
    Instance<ITokenService> _tokenService;

    @Inject
    Instance<IUserService> _userService;

    @Inject
    Instance<IEmailService> _emailService;

    @Inject
    Instance<ICryptoService> _cryptoService;

    @Inject
    Instance<IAppLogService> _log;

    @Inject
    UserPasswordResetRepository _userPasswordResetRepository;

    public boolean requestPasswordResetForUser(UserPasswordResetRequestDTO requestDTO) {
        UserEntity existingUser = _userService.get().getUserByEmail(requestDTO.email);
        if (existingUser == null || existingUser.IsBanned) {
            return false;
        }

        String passwordResetToken = _cryptoService.get().generateRandomString(PASSWORD_RESET_TOKEN_LENGTH);

        LocalDateTime expiresAt = LocalDateTime.now().plusHours(HOURS_UNTIL_PASSWORD_RESET_TOKEN_EXPIRES);

        UserPasswordResetEntity passwordResetUser = new UserPasswordResetEntity();
        passwordResetUser.ExpiresAt = expiresAt;
        passwordResetUser.User = existingUser;
        passwordResetUser.Token = _cryptoService.get().hashString(passwordResetToken);

        _userPasswordResetRepository.persist(passwordResetUser);

        UserPasswordResetEmailTemplate template = new UserPasswordResetEmailTemplate(existingUser.Email, passwordResetToken);

        boolean isEmailSent = _emailService.get().sendEmail(template.getToAddress(), template.getFromAddress(), template.getSubject(), template.getHTMLContent());

        return isEmailSent;
    }

    public boolean requestPasswordResetVerificationForUser(UserPasswordResetVerificationRequestDTO requestDTO) {
        UserEntity user = this._userService.get().getUserByEmail(requestDTO.email);
        if (user == null) {
            _log.get().warning("User Entity Not Found", null, null);
            return false;
        }

        UserPasswordResetEntity passwordResetUser = _userPasswordResetRepository.findByToken(requestDTO.token);
        if (passwordResetUser == null) {
            _log.get().warning("Password Reset User Not Found", null, null);
            return false;
        }

        long expiresAt = passwordResetUser.ExpiresAt.toEpochSecond(ZoneOffset.UTC);
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        boolean isExpired = now > expiresAt;

        if (isExpired) {
            _log.get().warning("Password Reset Token is Expired!", null, null);
            return false;
        }

        String newPassword = this._cryptoService.get().hashString(requestDTO.password);

        user.PasswordHash = newPassword;
        user.IsVerified = true;
        user.FailedLoginAttempts = 0;

        this._userService.get().updateUser(user);

        // TODO: should probably send another email to confirm that the password was changed.

        return true;
    }

}

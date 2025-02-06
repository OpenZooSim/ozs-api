package com.openzoosim.services.impl;

import com.openzoosim.config.ApplicationConfig;
import com.openzoosim.entities.UserEntity;
import com.openzoosim.exceptions.CryptoException;
import com.openzoosim.exceptions.NewUserNotUniqueException;
import com.openzoosim.models.dtos.UserRegistrationRequest;
import com.openzoosim.models.enums.UserRole;
import com.openzoosim.repositories.UserRepository;
import com.openzoosim.services.IAuthRegistrationService;
import com.openzoosim.services.ICryptoService;
import com.openzoosim.services.IEmailService;
import com.openzoosim.util.TokenUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthRegistrationServiceImpl implements IAuthRegistrationService {

    @Inject
    UserRepository userRepo;

    @Inject
    ApplicationConfig config;

    @Inject
    Instance<IEmailService> emailService;

    @Inject
    Instance<ICryptoService> cryptoService;

    @Transactional
    @Override
    public boolean register(UserRegistrationRequest request) throws CryptoException, NewUserNotUniqueException {

        UserEntity existingUser = userRepo.findByEmail(request.email().toLowerCase());

        if (existingUser != null) {
            throw new NewUserNotUniqueException();
        }

        String hashedPassword = this.cryptoService.get().hashPassword(request.password(), config.getAppKey());

        UserEntity newUser = new UserEntity();
        newUser.email = request.email().toLowerCase();
        newUser.username = request.username();
        newUser.password = hashedPassword;
        newUser.role = UserRole.FREE;
        userRepo.persist(newUser);

        String verificationToken = TokenUtil.generateVerificationToken(this.config.getAppKey(), Integer.parseInt(newUser.id.toString()));
        if (verificationToken == null) {
            return false;
        }

        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + verificationToken;
        String emailBody = "<p>Hi <b>" + newUser.username + "</b>,</p><p>Thank you for registering with MyRankedList. Please click the link below to verify your account:</p><p><a href=\"" + verificationUrl + "\">Verify Account</a></p>";

        boolean success = this.emailService.get().send(newUser.email, "OpenTriviaOnline - Verify Your Account", emailBody);

        return success;
    }

    @Transactional
    @Override
    public boolean verify(String token) {
        try {
            Integer userId = TokenUtil.verifyToken(this.config.getAppKey(), token);
            if (userId == -1) {
                return false;
            }
    
            UserEntity user = userRepo.findById(Long.parseLong(userId.toString()));
    
            user.email_verified = true;
            userRepo.persist(user);
    
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }    
}

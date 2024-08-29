package com.openzoosim.services.auth;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.models.dtos.UserRegistrationRequestDTO;
import com.openzoosim.models.dtos.UserRegistrationResponseDTO;
import com.openzoosim.models.interfaces.ICryptoService;
import com.openzoosim.models.interfaces.IEmailService;
import com.openzoosim.models.interfaces.ITokenService;
import com.openzoosim.models.interfaces.IUserRegistrationService;
import com.openzoosim.models.interfaces.IUserService;
import com.openzoosim.repositories.UserRepository;
import com.openzoosim.services.email.templates.NewUserRegistrationEmailTemplate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserRegistrationService implements IUserRegistrationService {

    @Inject
    Instance<IUserService> _userService;

    @Inject
    Instance<ITokenService> _tokenService;

    @Inject
    Instance<IEmailService> _emailService;

    @Inject
    Instance<ICryptoService> _cryptoService;

    @Inject
    UserRepository _userRepository;

    public UserRegistrationResponseDTO registerNewUser(UserRegistrationRequestDTO dto) {
        UserRegistrationResponseDTO res = new UserRegistrationResponseDTO();

        UserEntity user = _userService.get().getUserByEmail(dto.email);
        if (user != null) {
            res.email = user.Email;
            res.message = "User with email " + dto.email + " already exists! Try logging in!";
            res.status = 400;
            return res;
        }

        String hashedPassword = _cryptoService.get().hashString(dto.password);

        user = _userService.get().createNewUser(dto.email, hashedPassword, dto.name);

        String verificationToken = _tokenService.get().generateVerificationToken(user.Email);

        NewUserRegistrationEmailTemplate template = new NewUserRegistrationEmailTemplate(user.Email, verificationToken);

        boolean emailSentSuccessfully = _emailService.get().sendEmail(template.getToAddress(), template.getFromAddress(), template.getSubject(), template.getHTMLContent());

        res.email = user.Email;
        res.message = emailSentSuccessfully ? "User with email " + user.Email + " created! " +
                "Please check your email for account verification!" :
                "Your user was created, but an error occurred " +
                "when attempting to send you a verification email. Please use the FORGOT PASSWORD functionality to " +
                "reset your account or contact support for more help!";
        res.status = 201;
        return res;
    }

    public String verifyNewUser(String token) {
        String userEmail = _tokenService.get().verifyToken(token);

        if (userEmail == null) {
            return null;
        }

        UserEntity user = _userService.get().getUserByEmail(userEmail);
        if (user == null) {
            return null;
        }

        user.IsVerified = true;

        _userRepository.persist(user);

        return "User with email " + userEmail + " verified successfully! You may now login!";
    }

}

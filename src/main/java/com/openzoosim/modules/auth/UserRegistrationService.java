package com.openzoosim.modules.auth;

import com.openzoosim.modules.email.EmailService;
import com.openzoosim.modules.jwt.TokenService;
import com.openzoosim.modules.user.UserEntity;
import com.openzoosim.modules.user.UserRepository;
import com.openzoosim.modules.user.UserService;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserRegistrationService {

    @Inject
    UserService _userService;

    @Inject
    TokenService _tokenService;

    @Inject
    EmailService _emailService;

    @Inject
    UserRepository _userRepository;

    public UserRegistrationResponseDTO RegisterNewUser(UserRegistrationRequestDTO dto) {
        UserRegistrationResponseDTO res = new UserRegistrationResponseDTO();

        UserEntity user = _userService.GetUserByEmail(dto.email);
        if (user != null) {
            res.email = user.Email;
            res.message = "User with email " + dto.email + " already exists! Try logging in!";
            res.status = 400;
            return res;
        }

        String hashedPassword = BcryptUtil.bcryptHash(dto.password, 13);

        user = _userService.CreateNewUser(dto.email, hashedPassword, dto.name);

        String verificationToken = _tokenService.GenerateVerificationToken(user.Email);

        boolean emailSentSuccessfully = _emailService.SendEmail(
                user.Email,
                "do-not-reply@vistatable.com",
                "VistaTable - Verify Your Account",
                "<h2>Welcome to Vista Table!</h2> " +
                        "<p>To confirm your new account, please click the link below!</p>" +
                        "<p><a href=\"http://localhost:3000/auth/verify?token=" + verificationToken + "\">VERIFY ACCOUNT</a></p>"
        );

        res.email = user.Email;
        res.message = emailSentSuccessfully ? "User with email " + user.Email + " created! " +
                "Please check your email for account verification!" :
                "Your user was created, but an error occurred " +
                "when attempting to send you a verification email. Please use the FORGOT PASSWORD functionality to " +
                "reset your account or contact support for more help!";
        res.status = 201;
        return res;

    }

    public String VerifyNewUser(String token) {
        String userEmail = _tokenService.VerifyToken(token);

        if (userEmail == null) {
            return null;
        }

        UserEntity user = _userService.GetUserByEmail(userEmail);
        if (user == null) {
            return null;
        }

        user.IsVerified = true;

        _userRepository.persist(user);

        return "User with email " + userEmail + " verified successfully! You may now login!";
    }

}

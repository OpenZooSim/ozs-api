package com.openzoosim.services.auth;

import java.security.SecureRandom;

import com.openzoosim.models.interfaces.ICryptoService;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CryptoService implements ICryptoService {
    
    private static final int BCRYPT_ROUNDS = 13;

    public String hashString(String message) {
        return BcryptUtil.bcryptHash(message, BCRYPT_ROUNDS);
    }

    public boolean verifyHash(String message, String hash) {
        return BcryptUtil.matches(message, hash);
    }

        public String generateRandomString(int length) {
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();

        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            result.append(allowedCharacters.charAt(randomIndex));
        }

        return result.toString();
    }

}

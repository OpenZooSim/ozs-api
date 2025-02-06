package com.openzoosim.services.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import com.openzoosim.exceptions.CryptoException;
import com.openzoosim.services.ICryptoService;
import com.openzoosim.util.StringUtil;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CryptoServiceImpl implements ICryptoService {

    private static final int HASH_SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 12;

    @Override
    public String hashPassword(String password, String pepper) throws CryptoException {

        if (Objects.isNull(password) || Objects.isNull(pepper)) {
            throw new CryptoException("Password and Pepper must be supplied with non-null values!");
        }

        if (password.equals("") || pepper.equals("")) {
            throw new CryptoException("Password and Pepper must be supplied!");
        }

        byte[] randomBytes = StringUtil.generateRandomBytesWithLength(HASH_SALT_LENGTH);
        return BcryptUtil.bcryptHash(checksum256(pepper + password), ITERATION_COUNT, randomBytes);
    }

    @Override
    public boolean verifyPassword(String plainPassword, String hashedPassword, String pepper) throws CryptoException {
        if (Objects.isNull(plainPassword) || Objects.isNull(hashedPassword) || Objects.isNull(pepper)) {
            throw new CryptoException("Plain Password, Hashed Password, and Pepper must be supplied with non-null values!");
        }

        if (plainPassword.equals("") || hashedPassword.equals("") || pepper.equals("")) {
            throw new CryptoException("Plain Password, Hashed Password, and Pepper must be supplied!");
        }

        return BcryptUtil.matches(checksum256(pepper + plainPassword), hashedPassword);
    }

    @Override
    public String checksum256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
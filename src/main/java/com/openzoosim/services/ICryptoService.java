package com.openzoosim.services;

import com.openzoosim.exceptions.CryptoException;

public interface ICryptoService {
    String hashPassword(String password, String pepper) throws CryptoException;
    boolean verifyPassword(String plainPassword, String hashedPassword, String pepper) throws CryptoException;
    String checksum256(String input);
}

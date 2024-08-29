package com.openzoosim.models.interfaces;

public interface ICryptoService {
    public String hashString(String message);
    public boolean verifyHash(String message, String hash);
    public String generateRandomString(int length);
}

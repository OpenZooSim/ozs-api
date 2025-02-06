package com.openzoosim.util;

import java.security.SecureRandom;
import java.util.Base64;

import com.openzoosim.models.dtos.UserCredentials;

public class StringUtil {
    
    public static String bytesToString(byte[] bytes) {
        return new String(bytes);
    }

    public static byte[] generateRandomBytesWithLength(int length) {
        byte[] randomBytes = new byte[length];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }

    public static String base64Decode(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }

    public static String base64Encode(String plainString) {
        return Base64.getEncoder().encodeToString(plainString.getBytes());
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static UserCredentials parseBasicAuth(String authHeader) {
        String[] parts = authHeader.split(":");
        return new UserCredentials(parts[0], parts[1]);
    }

}

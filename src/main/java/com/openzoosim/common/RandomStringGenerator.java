package com.openzoosim.common;

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.-!#@";

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString(int length) {

        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            result.append(allowedCharacters.charAt(randomIndex));
        }

        return result.toString();
    }
}

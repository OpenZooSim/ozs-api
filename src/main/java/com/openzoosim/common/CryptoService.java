package com.openzoosim.common;

import io.quarkus.elytron.security.common.BcryptUtil;

public class CryptoService {
    
    private static final int BCRYPT_ROUNDS = 13;

    public static String HashString(String message) {
        return BcryptUtil.bcryptHash(message, BCRYPT_ROUNDS);
    }

    public static boolean VerifyHash(String message, String hash) {
        return BcryptUtil.matches(message, hash);
    }

}

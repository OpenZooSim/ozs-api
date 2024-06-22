package com.openzoosim;

import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.openzoosim.common.CryptoService;

@QuarkusTest
class CryptoTest {
    @Test
    void testCryptoHash() {

        String myPassword = "mySuperSecretPassword123!";
        String hash = CryptoService.HashString(myPassword);

        assertNotNull(hash);

        boolean isVerified = CryptoService.VerifyHash(myPassword, hash);

        assertTrue(isVerified);

        boolean isNotVerified = CryptoService.VerifyHash(myPassword + "_wrongpassword", hash);

        assertFalse(isNotVerified);

    }

    @Test
    void testRandomStringGenerator() {

        String myPassword = "mySuperSecretPassword123!";
        String hash = CryptoService.HashString(myPassword);

        assertNotNull(hash);

        boolean isVerified = CryptoService.VerifyHash(myPassword, hash);

        assertTrue(isVerified);

        boolean isNotVerified = CryptoService.VerifyHash(myPassword + "_wrongpassword", hash);

        assertFalse(isNotVerified);

    }    

}

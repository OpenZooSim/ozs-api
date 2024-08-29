package com.openzoosim;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.openzoosim.models.interfaces.ICryptoService;

@QuarkusTest
class CryptoTest {

    @Inject
    Instance<ICryptoService> _cryptoService;  

    @Test
    void testCryptoHash() {

        String myPassword = "mySuperSecretPassword123!";
        String hash = _cryptoService.get().hashString(myPassword);

        assertNotNull(hash);

        boolean isVerified = _cryptoService.get().verifyHash(myPassword, hash);

        assertTrue(isVerified);

        boolean isNotVerified = _cryptoService.get().verifyHash(myPassword + "_wrongpassword", hash);

        assertFalse(isNotVerified);

    }

    @Test
    void testRandomStringGenerator() {

        int randomStringLength = 64;

        String str1 = _cryptoService.get().generateRandomString(randomStringLength);
        String str2 = _cryptoService.get().generateRandomString(randomStringLength);

        assertNotNull(str1);
        assertNotNull(str2);

        boolean isEqual = str1.equals(str2);

        assertFalse(isEqual);

    }    

}

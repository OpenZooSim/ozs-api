package com.openzoosim;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.openzoosim.exceptions.CryptoException;
import com.openzoosim.services.ICryptoService;
import com.openzoosim.util.StringUtil;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class CryptoTest {

    @Inject
    Instance<ICryptoService> cryptoService;

    private static final String PLAIN_TEXT_PASSWORD = "test1P@ssw0rd!.Sup3rS3cret!!";
    private static final String PEPPER = "myS3cretPepperV@lue!";

    @Test
    void testCryptoHashingUniqueness() {

        try {
            String hashedValue1 = this.cryptoService.get().hashPassword(PLAIN_TEXT_PASSWORD, PEPPER);
            String hashedValue2 = this.cryptoService.get().hashPassword(PLAIN_TEXT_PASSWORD, PEPPER);
    
            assertThat(hashedValue1, not(equalTo(hashedValue2)));
    
            String hashedValue3 = this.cryptoService.get().hashPassword(PLAIN_TEXT_PASSWORD, PEPPER);
            String hashedValue4 = this.cryptoService.get().hashPassword(PLAIN_TEXT_PASSWORD, PEPPER);
    
            assertThat(hashedValue3, not(equalTo(hashedValue4)));
        } catch (CryptoException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testCryptoHashingInputs() {

        // Checking NULL safety
        try {
            String hashedValueNullInput = this.cryptoService.get().hashPassword(null, null);
        } catch (CryptoException e) {
            assertThat(e.getMessage(), equalTo("Password and Pepper must be supplied with non-null values!"));
        }

        try {
            // This should be unused because it should throw the exception.
            String hashedValueEmptyInput = this.cryptoService.get().hashPassword("", "");
        } catch (CryptoException e) {
            assertThat(e.getMessage(), equalTo("Password and Pepper must be supplied!"));
        }

        try {
            // This should be unused because it should throw the exception.
            boolean hashedValueEmptyInputResult = this.cryptoService.get().verifyPassword("", "", "");
        } catch (CryptoException e) {
            assertThat(e.getMessage(), equalTo("Plain Password, Hashed Password, and Pepper must be supplied!"));
        }

        // Ensure that different pepper values still generate different hashes even if the password is the same.
        try {
            String hashedValue1 = this.cryptoService.get().hashPassword(PLAIN_TEXT_PASSWORD, PEPPER);
            String hashedValue2 = this.cryptoService.get().hashPassword(PLAIN_TEXT_PASSWORD, PEPPER + "_modified");
    
            assertThat(hashedValue1, not(equalTo(hashedValue2)));
        } catch (CryptoException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCryptoHashingVerification() {

        try {
            String hashedValue1 = this.cryptoService.get().hashPassword(PLAIN_TEXT_PASSWORD, PEPPER);
            boolean isMatchingTrue = this.cryptoService.get().verifyPassword(PLAIN_TEXT_PASSWORD, hashedValue1, PEPPER);
    
            assertTrue(isMatchingTrue);
    
            boolean isMatchingFalse = this.cryptoService.get().verifyPassword(PLAIN_TEXT_PASSWORD, hashedValue1, PEPPER + "incorrectValue");
    
            assertTrue(!isMatchingFalse);
        } catch (CryptoException e) {
            e.printStackTrace();
        }

    }    

    @Test
    void testGeneratingRandomBytes() {
        int ARRAY_LENGTH = 16;
        byte[] bytes = StringUtil.generateRandomBytesWithLength(ARRAY_LENGTH);
        assertTrue(bytes.length == ARRAY_LENGTH);
    }       

}
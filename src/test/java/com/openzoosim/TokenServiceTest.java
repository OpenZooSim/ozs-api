package com.openzoosim;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.entities.UserTypeEntity;
import com.openzoosim.models.interfaces.ITokenService;

@QuarkusTest
class TokenServiceTest {

    @Inject
    Instance<ITokenService> _tokenService;  

    @Test
    void testVerificationToken() {

        String email = "an-example-user@example.com";

        String verificationToken = _tokenService.get().generateVerificationToken(email);

        assertNotNull(verificationToken);
        assertFalse(verificationToken.isEmpty());

        String tokenEmail = _tokenService.get().verifyToken(verificationToken);

        assertNotNull(tokenEmail);
        assertFalse(tokenEmail.isEmpty());
        assertTrue(tokenEmail.equals(email));

    }     

    @Test
    void testIDToken() {

        UserEntity user = new UserEntity();
        user.Email = "my-test-user@test.com";
        user.Username = "MyTestUser";
        user.id = 1;

        UserTypeEntity uType = new UserTypeEntity();
        uType.id = 1;
        uType.Name = "name";
        uType.Key = "key";

        user.UserType = uType;

        String userIDToken = _tokenService.get().generateIDToken(user);

        assertNotNull(userIDToken);
        assertFalse(userIDToken.isEmpty());
    }      

}

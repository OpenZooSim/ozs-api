package com.openzoosim;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import com.openzoosim.util.TokenUtil;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class TokenTest {

    private static final String JWT_SECRET = "jwt1P@ssw0rd!SECRET.Sup3rS3crettext!!";

    @Test
    void testVerificationTokenCreation() {

        try {
            int userId = 1001;
            String token = TokenUtil.generateVerificationToken(JWT_SECRET, userId);
            assertThat(token, not(equalTo(null)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }   

    @Test
    void testTokenVerification() {

        try {
            int userId = 1001;
            String token = TokenUtil.generateVerificationToken(JWT_SECRET, userId);
            int userIdFromToken = TokenUtil.verifyToken(JWT_SECRET, token);
            assertThat(userIdFromToken, equalTo(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }       

    @Test
    void testEnsureInvalidSecretsWillFailVerification() {

        try {
            int userId = 1001;
            String token = TokenUtil.generateVerificationToken(JWT_SECRET, userId);
            int userIdFromToken = TokenUtil.verifyToken(JWT_SECRET + "_INVALID_TOKEN", token);
            assertThat(userIdFromToken, equalTo(-1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }      

}
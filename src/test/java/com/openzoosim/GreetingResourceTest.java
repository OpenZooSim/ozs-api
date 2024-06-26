package com.openzoosim;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/health")
          .then()
             .statusCode(200)
             .body(containsString("ok"));
    }

}
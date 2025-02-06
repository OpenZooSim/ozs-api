package com.openzoosim.redis;

import java.time.Instant;
import java.util.Date;

public class UserSessionPOJO {
    public long userId; 
    public String email;
    public Date sessionCreated;
    public String role;

    public UserSessionPOJO(long userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.sessionCreated = Date.from(Instant.now());
    }

    public UserSessionPOJO() {
        this.sessionCreated = Date.from(Instant.now());
    }
}

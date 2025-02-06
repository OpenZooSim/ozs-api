package com.openzoosim.models.dtos;

import java.time.Instant;

public record JWTOptions(String secret, Instant expiresAt, int userId) {
}

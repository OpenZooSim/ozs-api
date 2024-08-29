package com.openzoosim.services.auth;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.openzoosim.models.interfaces.IUUIDService;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UUIDService implements IUUIDService {

    @Override
    public String generateRandomToken() {
        UUID uuid = Generators.timeBasedEpochRandomGenerator().generate();
        return uuid.toString();
    }
    
}

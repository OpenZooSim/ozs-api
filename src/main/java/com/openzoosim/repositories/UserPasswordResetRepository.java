package com.openzoosim.repositories;

import com.openzoosim.entities.UserPasswordResetEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserPasswordResetRepository implements PanacheRepository<UserPasswordResetEntity> {

    public UserPasswordResetEntity findByToken(String token){
        return find("Token", token).firstResult();
    }
}


package com.openzoosim.repositories;

import com.openzoosim.entities.UserSessionEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserSessionRepository implements PanacheRepository<UserSessionEntity> {
    public UserSessionEntity findByTokenID(String tokenID){
        return find("SessionTokenID", tokenID).firstResult();
    }
}


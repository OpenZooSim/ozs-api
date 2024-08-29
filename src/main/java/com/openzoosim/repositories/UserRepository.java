package com.openzoosim.repositories;

import com.openzoosim.entities.UserEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    public UserEntity findByEmail(String email) {
        return find("Email", email.toLowerCase()).firstResult();
    }
}

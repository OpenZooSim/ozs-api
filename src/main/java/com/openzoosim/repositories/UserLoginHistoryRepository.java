package com.openzoosim.repositories;

import com.openzoosim.entities.UserLoginHistoryEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserLoginHistoryRepository implements PanacheRepository<UserLoginHistoryEntity> {
}


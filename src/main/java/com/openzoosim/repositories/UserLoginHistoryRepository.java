package com.openzoosim.repositories;

import com.openzoosim.entities.UserEntity;
import com.openzoosim.entities.UserLoginHistoryEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserLoginHistoryRepository implements PanacheRepository<UserLoginHistoryEntity> {
    
    @Transactional
    public void createLoginHistoryEntry(UserEntity user) { 
        UserLoginHistoryEntity userLoginHistoryEntity = new UserLoginHistoryEntity();
        userLoginHistoryEntity.user = user;
        persist(userLoginHistoryEntity);
    }
}

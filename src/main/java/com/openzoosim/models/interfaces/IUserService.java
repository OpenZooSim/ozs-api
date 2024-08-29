package com.openzoosim.models.interfaces;

import com.openzoosim.entities.UserEntity;

public interface IUserService {
    public UserEntity getUserByID(Long id);
    public UserEntity getUserByEmail(String email);
    public UserEntity createNewUser(String email, String password, String name);
    public void updateUser(UserEntity user);
}

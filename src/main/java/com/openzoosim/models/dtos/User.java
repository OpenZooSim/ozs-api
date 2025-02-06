package com.openzoosim.models.dtos;

import java.util.Date;

import com.openzoosim.entities.UserEntity;

public class User {
    public Long id;
    public Date created_at;
    public Date updated_at;
    public String username;
    public String email;

    public User() {}

    public User(UserEntity u) {
        id = u.id;
        created_at = u.created_at;
        updated_at = u.updated_at;
        username = u.username;
        email = u.email;
    }
}
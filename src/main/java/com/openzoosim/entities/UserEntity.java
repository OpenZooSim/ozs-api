package com.openzoosim.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends BaseMRLEntity {
    @Column(name = "username")
    public String username;
    @Column(name = "email")
    public String email;
    @Column(name = "password")
    public String password;
    @Column(name = "email_verified")
    public boolean email_verified;
    @Column(name = "role")
    public String role;
    @Column(name = "is_api_user")
    public boolean is_api_user; 
    @Column(name = "api_key")
    public String api_key;  
    @Column(name = "last_login")
    public Date last_login;
    @Column(name = "last_api_access")
    public Date last_api_access;             
}
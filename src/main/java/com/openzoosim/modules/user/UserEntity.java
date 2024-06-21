package com.openzoosim.modules.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import com.openzoosim.common.BaseEntity;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(name = "email")
    public String Email;
    @Column(name = "password")
    public String Password;
    @Column(name = "name")
    public String Name;
    @Column(name = "verified")
    public boolean Verified;
    @Column(name = "last_login")
    public LocalDateTime LastLogin;
}


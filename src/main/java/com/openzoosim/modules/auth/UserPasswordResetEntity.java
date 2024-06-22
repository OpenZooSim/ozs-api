package com.openzoosim.modules.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import com.openzoosim.common.BaseEntity;
import com.openzoosim.modules.user.UserEntity;

@Entity
@Table(name = "user_password_resets")
public class UserPasswordResetEntity extends BaseEntity {
    @Column(name = "token")
    public String Token;
    @Column(name = "expires_at")
    public LocalDateTime ExpiresAt;

    @ManyToOne()
    @JoinColumn(name="user_id")
    public UserEntity User;
}


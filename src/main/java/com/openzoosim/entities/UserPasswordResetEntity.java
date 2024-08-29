package com.openzoosim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

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


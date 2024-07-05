package com.openzoosim.modules.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import com.openzoosim.common.BaseEntity;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(name = "email")
    public String Email;
    @Column(name = "username")
    public String Username;
    @Column(name = "is_verified")
    public boolean IsVerified;
    @Column(name = "password_hash")
    public String PasswordHash;
    @Column(name = "last_login")
    public LocalDateTime LastLogin;
    @Column(name = "failed_login_attempts")
    public int FailedLoginAttempts;
    @Column(name = "is_banned")
    public boolean IsBanned;   
    @Column(name = "ban_reason")
    public String BanReason;

    @ManyToOne()
    @JoinColumn(name="user_type_id")
    public UserTypeEntity UserType;
}


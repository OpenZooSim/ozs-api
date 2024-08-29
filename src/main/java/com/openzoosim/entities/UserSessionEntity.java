package com.openzoosim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
public class UserSessionEntity extends BaseEntity {
    @Column(name = "session_token")
    public String SessionTokenID;
    @Column(name = "expires_at")
    public LocalDateTime ExpiresAt;
    @Column(name = "ip")
    public String IP;
    @Column(name = "device_thumbprint")
    public String DeviceThumbprint;      

    @ManyToOne()
    @JoinColumn(name="user_id")
    public UserEntity User;
}
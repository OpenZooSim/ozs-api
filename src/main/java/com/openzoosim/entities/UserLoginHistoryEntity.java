package com.openzoosim.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_login_history")
public class UserLoginHistoryEntity extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name="user_id")
    public UserEntity User;
}
package com.openzoosim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_login_history")
public class UserLoginHistoryEntity extends BaseMRLEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity user;

    @Column(name = "user_id", insertable = false, updatable = false)
    public Long user_id;    
}

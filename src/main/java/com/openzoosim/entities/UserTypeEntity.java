package com.openzoosim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_types")
public class UserTypeEntity extends BaseEntity {
    @Column(name = "name")
    public String Name;
    @Column(name = "key")
    public String Key;
}



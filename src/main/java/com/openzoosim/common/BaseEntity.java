package com.openzoosim.common;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;
    @CreationTimestamp
    @Column(name = "created")
    public LocalDateTime Created;
    @UpdateTimestamp
    @Column(name = "updated")
    public LocalDateTime Updated;

    @Column(name = "is_archived")
    public boolean IsArchived;
}

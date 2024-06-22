package com.openzoosim.common;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;
    @CreationTimestamp
    @Column(name = "created_at")
    public LocalDateTime CreatedAt;
    @UpdateTimestamp
    @Column(name = "modified_at")
    public LocalDateTime ModifiedAt;

    @Column(name = "is_archived")
    public boolean IsArchived;
}

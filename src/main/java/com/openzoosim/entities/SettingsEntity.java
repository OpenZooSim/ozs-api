package com.openzoosim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "settings")
public class SettingsEntity extends BaseMRLEntity {
    @Column(name = "is_email_enabled")
    public boolean is_email_enabled;
    @Column(name = "is_maintenance_mode")
    public boolean is_maintenance_mode;    
    @Column(name = "maintenance_message")
    public String maintenance_message;    
}
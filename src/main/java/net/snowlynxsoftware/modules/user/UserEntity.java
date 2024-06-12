package net.snowlynxsoftware.modules.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import net.snowlynxsoftware.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(name = "email")
    public String Email;
    @Column(name = "password")
    public String Password;
    @Column(name = "name")
    public String Name;
    @Column(name = "verified")
    public boolean Verified;
    @Column(name = "last_login")
    public LocalDateTime LastLogin;
}


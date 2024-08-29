package com.openzoosim.models.dtos;

import java.time.LocalDateTime;

import com.openzoosim.entities.UserEntity;

public class UserDTO {
    
    public long id;
    public LocalDateTime createdAt;   
    public LocalDateTime modifiedAt;  
    public boolean isArchived;       
    public boolean isVerified;
    public LocalDateTime lastLogin;
    public long userTypeId;
    public String userTypeName;
    public String userTypeKey;
    public String username;
    public String email;
    public boolean isBanned;
    public String banReason;
    public int failedLoginAttempts;

    public UserDTO() {}

    public UserDTO(UserEntity _entity) {
        this.id = _entity.id;
        this.createdAt = _entity.CreatedAt;
        this.modifiedAt = _entity.ModifiedAt;
        this.isArchived = _entity.IsArchived;
        this.isVerified = _entity.IsVerified;
        this.lastLogin = _entity.LastLogin;
        this.userTypeId = _entity.UserType.id;
        this.userTypeName = _entity.UserType.Name;
        this.userTypeKey = _entity.UserType.Key;
        this.username = _entity.Username;
        this.email = _entity.Email;
        this.isBanned = _entity.IsBanned;
        this.banReason = _entity.BanReason;
        this.failedLoginAttempts = _entity.FailedLoginAttempts;
    }

}

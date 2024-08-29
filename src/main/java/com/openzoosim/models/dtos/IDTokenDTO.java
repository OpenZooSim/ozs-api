package com.openzoosim.models.dtos;

import com.openzoosim.entities.UserEntity;

public class IDTokenDTO {
    public long id;
    public long userTypeId;
    public String userTypeName;
    public String userTypeKey;
    public String username;
    public String email;

    public IDTokenDTO(UserEntity _entity) {
        this.id = _entity.id;
        this.userTypeId = _entity.UserType.id;
        this.userTypeName = _entity.UserType.Name;
        this.userTypeKey = _entity.UserType.Key;
        this.username = _entity.Username;
        this.email = _entity.Email;
    }
}



package com.openzoosim.models.interfaces;

public interface IAuthorizeService {
    public int authorizedUserID(String token, String ip, String deviceThumprint);
}

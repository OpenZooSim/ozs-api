package com.openzoosim.models.interfaces;

public interface IEnvService {
    public String GetAppVersion();
    public boolean GetIsTestMode();
    public String GetJWTTokenSecret();
    public long GetVerificationExpiresInSecs();
    public long GetSessionTokenExpiresInSecs();
    public String GetSessionCookieDomain();    
    public String GetEmailProviderAPIKey();
}

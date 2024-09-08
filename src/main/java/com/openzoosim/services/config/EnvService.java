package com.openzoosim.services.config;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.openzoosim.models.interfaces.IEnvService;

@ApplicationScoped
public class EnvService implements IEnvService {
    @ConfigProperty(name = "mrl.app.version")
    String MRL_APP_VERSION;  

    @ConfigProperty(name = "mrl.jwt.secret")
    String JWT_TOKEN_SECRET;

    @ConfigProperty(name = "mrl.verification.expires.in.secs")
    String VERIFICATION_EXPIRES_IN_SECS;

    @ConfigProperty(name = "mrl.session.token.expires.in.secs")
    String SESSION_TOKEN_EXPIRES_IN_SECS;

    @ConfigProperty(name = "mrl.session.cookie.domain")
    String SESSION_COOKIE_DOMAIN;    

    @ConfigProperty(name = "email.provider.api.key")
    String EMAIL_PROVIDER_API_KEY;

    public String GetAppVersion() { return MRL_APP_VERSION; }

    public String GetJWTTokenSecret() {
        return JWT_TOKEN_SECRET;
    }

    public long GetVerificationExpiresInSecs() {
        return Long.parseLong(VERIFICATION_EXPIRES_IN_SECS);
    }    

    public long GetSessionTokenExpiresInSecs() {
        return Long.parseLong(SESSION_TOKEN_EXPIRES_IN_SECS);
    }     
    
    public String GetSessionCookieDomain() {
        return SESSION_COOKIE_DOMAIN;
    }      

    public String GetEmailProviderAPIKey() { return EMAIL_PROVIDER_API_KEY; }
}

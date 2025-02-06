package com.openzoosim.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ApplicationConfig {

    @ConfigProperty(name = "oto_app_key", defaultValue = "_DEFAULT_")
    String appKey;

    public String getAppKey() {
        return appKey;
    }

    @ConfigProperty(name = "oto_email_provider_key", defaultValue = "SG.xyz")
    String emailProviderKey;

    public String getEmailProviderKey() {
        return emailProviderKey;
    }    
}
package com.openzoosim.common;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

// These do not encompass all ENV vars--
// but only the ones that we may need to reference directly in code.
@ApplicationScoped
public class EnvService {
    @ConfigProperty(name = "ozs.app.version")
    String OZS_APP_VERSION;

    @ConfigProperty(name = "ozs.jwt.secret")
    String JWT_TOKEN_SECRET;

    @ConfigProperty(name = "email.provider.api.key")
    String EMAIL_PROVIDER_API_KEY;

    public String GetAppVersion() { return OZS_APP_VERSION; }

    public String GetJWTTokenSecret() {
        return JWT_TOKEN_SECRET;
    }

    public String GetEmailProviderAPIKey() { return EMAIL_PROVIDER_API_KEY; }
}

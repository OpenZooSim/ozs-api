package com.openzoosim.common;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

// These do not encompass all ENV vars--
// but only the ones that we may need to reference directly in code.
@ApplicationScoped
public class EnvService {
    @ConfigProperty(name = "ozs.jwt.secret")
    String JWT_TOKEN_SECRET;

    @ConfigProperty(name = "sendgrid.api.key")
    String SENDGRID_API_KEY;

    public String GetJWTTokenSecret() {
        return JWT_TOKEN_SECRET;
    }

    public String GetSendgridAPIKey() { return SENDGRID_API_KEY; }
}

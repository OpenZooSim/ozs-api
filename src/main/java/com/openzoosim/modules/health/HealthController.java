package com.openzoosim.modules.health;

import com.openzoosim.common.EnvService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/health")
public class HealthController {

    @Inject
    EnvService _envService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String GetHealthCheck() throws Exception {
        return "ok:" + _envService.GetAppVersion();
    }
}

package com.openzoosim.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/health")
@ApplicationScoped
public class HealthController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "ok";
    }

}



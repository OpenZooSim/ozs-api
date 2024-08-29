package com.openzoosim.controllers.api;

import com.openzoosim.middleware.Authorize;
import com.openzoosim.models.interfaces.IAppLogService;
import com.openzoosim.models.interfaces.IEnvService;

import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/health")
public class HealthController {

    @Inject
    RoutingContext _routingCTX;

    @Inject
    Instance<IEnvService> _envService;

    @Inject
    Instance<IAppLogService> _log;    

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHealthCheck() {
        _log.get().info("health check success", "HealthController.getHealthCheck");
        return "ok:" + _envService.get().GetAppVersion();
    }

    @Path("/auth-test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Authorize
    @Transactional
    public String testAuth() {
        Object id = _routingCTX.get("user_id");
        return "userid:" + id;
    }   
}

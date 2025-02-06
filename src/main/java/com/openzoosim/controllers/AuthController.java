package com.openzoosim.controllers;

import java.util.Map;

import com.openzoosim.annotations.Authorize;
import com.openzoosim.config.ApplicationConfig;
import com.openzoosim.models.dtos.UserLoginResponse;
import com.openzoosim.models.dtos.UserRegistrationRequest;
import com.openzoosim.redis.UserSessionPOJO;
import com.openzoosim.services.IAppLogService;
import com.openzoosim.services.IAuthLoginService;
import com.openzoosim.services.IAuthRegistrationService;

import io.netty.util.internal.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthController {

    @Inject
    ApplicationConfig config;

    @Inject
    Instance<IAuthRegistrationService> registrationService;

    @Inject
    Instance<IAuthLoginService> loginService;

    @Inject
    Instance<IAppLogService> logService;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("Authorization") String authHeaderValue) {

        try {
            if (StringUtil.isNullOrEmpty(authHeaderValue)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Authorization header is required")
                        .build();
            }

            UserLoginResponse response = loginService.get().login(authHeaderValue);

            return Response.status(Response.Status.OK)
                    .entity(response)
                    .build();
        } catch (Exception e) {
            this.logService.get().error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserRegistrationRequest request) {

        try {
            if (request.email() == null || request.password() == null || request.username() == null || request.email().isEmpty() || request.password().isEmpty() || request.username().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Email, password, and username are required.")
                        .build();
            }

            boolean success = registrationService.get().register(request);

            if (success) {
                Map<String, String> response = Map.of(
                        "message", "User registered successfully! Please check your email for verification!"
                );

                return Response.status(Response.Status.CREATED)
                        .entity(response)
                        .build();
            } else {
                throw new Exception("An error occurred when attempting to register a new user!");
            }
        } catch (Exception e) {
            this.logService.get().error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/verify")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verify(@QueryParam("token") String token) {

        try {
            if (token == null || token.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("token is required")
                        .build();
            }

            boolean success = registrationService.get().verify(token);

            if (success) {
                Map<String, String> response = Map.of(
                        "message", "User verified successfully! You may now login!"
                );

                return Response.status(Response.Status.OK)
                        .entity(response)
                        .build();
            } else {
                throw new Exception("An error occurred when attempting to verify the user!");
            }
        } catch (Exception e) {
            this.logService.get().error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }    

    @GET
    @Path("/token")
    @Authorize
    @Produces(MediaType.APPLICATION_JSON)
    public Response sessionTokenUser(@Context ContainerRequestContext requestContext) {

        try {
            
            UserSessionPOJO userSession = (UserSessionPOJO) requestContext.getProperty("sessionUser");

            return Response.status(Response.Status.OK)
            .entity(userSession)
            .build();
        } catch (Exception e) {
            this.logService.get().error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }   

}


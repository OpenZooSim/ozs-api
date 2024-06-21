package com.openzoosim.modules.auth;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    @Inject
    UserRegistrationService _userRegistrationService;

    @Inject
    AuthLoginService _loginService;

    @Path("/register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response RegisterNewUser(UserRegistrationRequestDTO req) {
        if (
                req.email == null ||
                req.email.isEmpty() ||
                req.password == null ||
                req.password.isEmpty() ||
                req.name == null ||
                req.name.isEmpty()
        ) {
            throw new WebApplicationException("Missing Required Fields: [email, password, name]", 400);
        }

        UserRegistrationResponseDTO result = _userRegistrationService.RegisterNewUser(req);

        return Response.status(result.status).entity(result).build();
    }

    @Path("/verify")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String VerifyNewUser(@QueryParam("token")String token) {
        if (
                token == null || token.isEmpty()
        ) {
            throw new WebApplicationException("Missing Required Fields: [token]", 400);
        }

        String result = _userRegistrationService.VerifyNewUser(token);

        if (result == null) {
            throw new WebApplicationException("Invalid Token", 401);
        } else {
            return result;
        }
    }

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response Login(@HeaderParam("authorization")String authHeader) {
        if (
                authHeader == null || authHeader.isEmpty()
        ) {
            throw new WebApplicationException("UNAUTHORIZED", 401);
        }

        try {
            AuthLoginResponseDTO res = _loginService.Login(authHeader);

            NewCookie accessCookie = new NewCookie. Builder("x-access-token")
                    .path("/")
                    .domain("localhost")
                    .sameSite(NewCookie.SameSite.LAX)
                    .httpOnly(true)
                    .value(res.access_token)
                    .maxAge(290)
                    .build();
            return Response.status(200).cookie(accessCookie).entity(res).build();
        } catch (Exception ex) {
            throw new WebApplicationException(ex.getMessage(), 401);
        }
    }
}


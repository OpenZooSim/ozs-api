package com.openzoosim.controllers.api;

import java.util.HashMap;
import java.util.Map;

import com.openzoosim.middleware.Authorize;
import com.openzoosim.models.dtos.AuthLoginResponseDTO;
import com.openzoosim.models.dtos.UserPasswordResetRequestDTO;
import com.openzoosim.models.dtos.UserPasswordResetVerificationRequestDTO;
import com.openzoosim.models.dtos.UserRegistrationRequestDTO;
import com.openzoosim.models.dtos.UserRegistrationResponseDTO;
import com.openzoosim.models.interfaces.IAppLogService;
import com.openzoosim.models.interfaces.IAuthLoginService;
import com.openzoosim.models.interfaces.IEnvService;
import com.openzoosim.models.interfaces.IUserPasswordResetService;
import com.openzoosim.models.interfaces.IUserRegistrationService;

import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/v1/auth")
public class AuthController {

    @Inject
    Instance<IUserRegistrationService> _userRegistrationService;

    @Inject
    Instance<IUserPasswordResetService> _userPasswordResetService;

    @Inject
    Instance<IAuthLoginService> _loginService;

    @Inject
    Instance<IAppLogService> _logService;    

    @Inject
    Instance<IEnvService> _envService;

    @Inject
    RoutingContext _context;

    @Path("/register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerNewUser(UserRegistrationRequestDTO req) {
        if (
                req.email == null ||
                req.email.isEmpty() ||
                req.password == null ||
                req.password.isEmpty() ||
                req.name == null ||
                req.name.isEmpty()
        ) {
            _logService.get().warning("Missing required fields!", "AuthController.registerNewUser", null);
            throw new WebApplicationException("Missing Required Fields: [email, password, name]", 400);
        }

        try {
            UserRegistrationResponseDTO result = _userRegistrationService.get().registerNewUser(req);
            return Response.status(result.status).entity(result).build();
        } catch (Exception ex) {
            _logService.get().warning("An error occurred when attempting to register a new user", "AuthController.registerNewUser", ex);
            throw new WebApplicationException(ex.getMessage(), 500);
        }
    }

    @Path("/verify")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String verifyNewUser(@QueryParam("token")String token) {
        if (
                token == null || token.isEmpty()
        ) {
            _logService.get().warning("Token is required!", "AuthController.verifyNewUser", null);
            throw new WebApplicationException("Missing Required Fields: [token]", 400);
        }

        String result = _userRegistrationService.get().verifyNewUser(token);

        if (result == null) {
            _logService.get().warning("Token is invalid!", "AuthController.verifyNewUser", null);
            throw new WebApplicationException("Invalid Token", 401);
        } else {
            return result;
        }
    }

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response login(@HeaderParam("authorization")String authHeader, @HeaderParam("user-agent")String userAgent) {
        if (
                authHeader == null || authHeader.isEmpty()
        ) {
            _logService.get().warning("Basic auth header is required!", "AuthController.login", null);
            throw new WebApplicationException("UNAUTHORIZED", 401);
        }

        try {
            String ip = _context.request().remoteAddress().host();
            AuthLoginResponseDTO res = _loginService.get().login(authHeader, userAgent, ip);

            NewCookie sessionCookie = new NewCookie. Builder("mrl-session-token")
                    .path("/")
                    .domain(_envService.get().GetSessionCookieDomain())
                    .sameSite(NewCookie.SameSite.LAX)
                    .httpOnly(true)
                    .value(res.session_token)
                    .maxAge(Integer.parseInt(Long.toString(_envService.get().GetSessionTokenExpiresInSecs())))
                    .build();
            return Response.status(200).cookie(sessionCookie).entity(res).build();
        } catch (Exception ex) {
            _logService.get().warning(ex.getMessage(), "AuthController.login", ex);
            throw new WebApplicationException(ex.getMessage(), 401);
        }
    }

    @Path("/logout")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response logout() {
        try {
            NewCookie logoutCookie = new NewCookie. Builder("mrl-session-token")
                    .path("/")
                    .domain(_envService.get().GetSessionCookieDomain())
                    .sameSite(NewCookie.SameSite.LAX)
                    .httpOnly(true)
                    .value("")
                    .maxAge(1)
                    .build();
                    
            return Response.status(200).cookie(logoutCookie).entity("ok").build();
        } catch (Exception ex) {
            _logService.get().warning(ex.getMessage(), "AuthController.logout", ex);
            throw new WebApplicationException(ex.getMessage(), 401);
        }
    }

    @Path("/session")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorize
    @Transactional
    public Response getSessionTokenInfo() {
        try {
            Object id = _context.get("user_id");
            String idToken = _loginService.get().getIDTokenForUserID(Long.parseLong(id.toString()));
                    
            Map<String, String> responseEntity = new HashMap<String, String>();
            responseEntity.put("id_token", idToken);

            return Response.status(200).entity(responseEntity).build();
        } catch (Exception ex) {
            _logService.get().warning(ex.getMessage(), "AuthController.getSessionTokenInfo", ex);
            throw new WebApplicationException(ex.getMessage(), 401);
        }
    }

    @Path("/password-reset-request")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String passwordResetRequest(UserPasswordResetRequestDTO req) {
        if (
                req.email == null || req.email.isEmpty()
        ) {
            _logService.get().warning("Email is required!", "AuthController.passwordResetRequest", null);
            throw new WebApplicationException("BAD REQUEST - email is required!", 400);
        }

        try {
            _userPasswordResetService.get().requestPasswordResetForUser(req);

            String messageResponse = "If you provided an email that can be found in our system, you will receive " +
            "an email with further instructions. Please note that if you have been banned, this process will not work!";
            return messageResponse;
        } catch (Exception ex) {
            _logService.get().warning(ex.getMessage(), "AuthController.passwordResetRequest", ex);
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }   

    @Path("/password-reset-verify")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String passwordResetVerifyRequest(UserPasswordResetVerificationRequestDTO req) {
        if (
                req.token == null || req.token.isEmpty() || req.password.isEmpty()
        ) {
            _logService.get().warning("Token and Password are required!", "AuthController.passwordResetVerifyRequest", null);
            throw new WebApplicationException("BAD REQUEST - [email,password] are required!", 400);
        }

        try {
            boolean result = _userPasswordResetService.get().requestPasswordResetVerificationForUser(req);

            String messageResponse = "";

            if (result) {
                messageResponse = "SUCCESS";
            } else {
                messageResponse = "FAILURE";
            }

            return messageResponse;
        } catch (Exception ex) {
            _logService.get().warning(ex.getMessage(), "AuthController.passwordResetVerifyRequest", ex);
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }   
    
    
}


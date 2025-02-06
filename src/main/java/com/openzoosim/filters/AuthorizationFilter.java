package com.openzoosim.filters;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

import com.openzoosim.annotations.Authorize;
import com.openzoosim.redis.UserSessionPOJO;
import com.openzoosim.services.IUserSessionService;

@Authorize
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Inject
    Instance<IUserSessionService> sessionService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String sessionTokenHeader = requestContext.getHeaderString("Authorization");

        if (sessionTokenHeader == null || sessionTokenHeader.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Session token is required")
                    .build());
            return;
        }

        String sessionToken = sessionTokenHeader.replace("Bearer ", "");

        if (sessionToken == null || sessionToken.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Session token is required")
                    .build());
            return;
        }

        UserSessionPOJO sessionUser = this.sessionService.get().getUserSession(sessionToken);

        if (sessionUser == null || sessionUser.userId == 0) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid session token")
                    .build());
            return;
        }

        requestContext.setProperty("sessionUser", sessionUser);
    }
}

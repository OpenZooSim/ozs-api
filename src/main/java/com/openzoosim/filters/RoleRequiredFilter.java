package com.openzoosim.filters;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;

import com.openzoosim.annotations.RoleRequired;
import com.openzoosim.redis.UserSessionPOJO;

@RoleRequired(roleName = "")
@Provider
@Priority(Priorities.AUTHORIZATION)
public class RoleRequiredFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        var userSession = (UserSessionPOJO) requestContext.getProperty("sessionUser");

        if (userSession == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Session token is required")
                    .build());
            return;
        }

        Method method = resourceInfo.getResourceMethod();
        RoleRequired roleRequired = method.getAnnotation(RoleRequired.class);

        if (roleRequired == null) {
            roleRequired = resourceInfo.getResourceClass().getAnnotation(RoleRequired.class);
        }

        if (roleRequired != null) {
            var requiredRole = roleRequired.roleName();
            var userRole = userSession.role;

            if (!requiredRole.equals(userRole)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("You don't have the required role to access this resource")
                        .build());
                return;
            }
        }

    }
}


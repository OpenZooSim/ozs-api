package com.openzoosim.modules.auth;

import io.quarkus.security.UnauthorizedException;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Authorize
@Priority(1001)
@Interceptor
public class AuthorizeInterceptor {

    @Inject
    RoutingContext _routingContext;

    @Inject
    AuthorizeService _authorizeService;

    @AroundInvoke
    Object authorizeInvocation(InvocationContext context) {
        try {
            Cookie cookie = _routingContext.request().getCookie("x-access-token");
            String token = cookie.getValue();
            boolean isAuthorized = _authorizeService.isAuthorized(token);
            if (!isAuthorized) {
                throw new UnauthorizedException("Unauthorized.");
            }
            return context.proceed();
        } catch (Exception ex) {
            throw new UnauthorizedException(ex.getMessage());
        }
    }

}

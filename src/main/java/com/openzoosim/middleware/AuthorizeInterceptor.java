package com.openzoosim.middleware;

import java.util.Base64;

import com.openzoosim.models.interfaces.IAuthorizeService;

import io.quarkus.logging.Log;
import io.quarkus.security.UnauthorizedException;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Instance;
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
    Instance<IAuthorizeService> _authorizeService;

    @AroundInvoke
    Object authorizeInvocation(InvocationContext context) {
        try {
            Cookie cookie = _routingContext.request().getCookie("mrl-session-token");
            String encodedToken = cookie.getValue();
            byte[] decodedBytes = Base64.getDecoder().decode(encodedToken);
            String token = new String(decodedBytes);
            String ip = _routingContext.request().remoteAddress().host();
            String deviceThumbprint = _routingContext.request().getHeader("user-agent");
            int authorizedUserID = _authorizeService.get().authorizedUserID(token, ip, deviceThumbprint);
            if (authorizedUserID == 0) {
                throw new UnauthorizedException("Unauthorized.");
            }
            _routingContext.put("user_id", authorizedUserID);
            return context.proceed();
        } catch (Exception ex) {
            Log.error(ex.getMessage());
            throw new UnauthorizedException(ex.getMessage());
        }
    }

    public static Object[] addElement(Object[] originalArray, Object newElement) {
        Object[] newArray = new Object[originalArray.length + 1];

        System.arraycopy(originalArray, 0, newArray, 0, originalArray.length);

        newArray[originalArray.length] = newElement;

        return newArray;
    }

}

package com.openzoosim.services.logging;

import com.openzoosim.models.interfaces.IAppLogService;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppLogService implements IAppLogService {

    @Override
    public void info(String message, String details) {
        Log.info(message + " - " + details);
    }

    @Override
    public void warning(String message, String details, Throwable t) {
        Log.warn(message + " - " + details, t);
    }

    @Override
    public void error(String message, String details, Throwable t) {
        Log.error(message + " - " + details, t);
    }

}

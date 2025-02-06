package com.openzoosim.services.impl;

import org.jboss.logging.Logger;

import com.openzoosim.services.IAppLogService;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppLogServiceImpl implements IAppLogService {

    private static final Logger log = Logger.getLogger(AppLogServiceImpl.class);

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void info(String message, Throwable t) {
        log.info(message, t);
    }

    @Override
    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    @Override
    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    @Override
    public void debug(String message, Throwable t) {
        log.debug(message, t);
    }
    
}

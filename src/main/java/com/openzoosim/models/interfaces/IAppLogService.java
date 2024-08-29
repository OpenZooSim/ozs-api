package com.openzoosim.models.interfaces;

public interface IAppLogService {
    public void info(String message, String details);
    public void warning(String message, String details, Throwable t);
    public void error(String message, String details, Throwable t);
}

package com.openzoosim.services;

public interface IAppLogService {
        void info(String message);
        void error(String message);
        void warn(String message);
        void debug(String message);

        void info(String message, Throwable t);
        void error(String message, Throwable t);
        void warn(String message, Throwable t);
        void debug(String message, Throwable t);
}

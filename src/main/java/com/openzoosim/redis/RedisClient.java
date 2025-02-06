package com.openzoosim.redis;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;

@ApplicationScoped
public class RedisClient {

    private ValueCommands<String, UserSessionPOJO> userSessions;
    private ValueCommands<String, SettingsPOJO> settings;

    public RedisClient(RedisDataSource ds) {
        userSessions = ds.value(UserSessionPOJO.class);
        settings = ds.value(SettingsPOJO.class);
    }

    public UserSessionPOJO getUserSession(String key) {
        UserSessionPOJO value = userSessions.get(key); 
        if (value == null) {
            return new UserSessionPOJO();
        }
        return value;
    }

    public void setUserSession(String key, UserSessionPOJO value) {
        userSessions.setex(key, 86400, value); 
    }

    public SettingsPOJO getCachedSettings() {
        return settings.get("settings"); 
    }

    public void setSettings(SettingsPOJO value) {
        settings.setex("settings", 3600, value); 
    }
}

package com.openzoosim.services.impl;

import com.openzoosim.redis.RedisClient;
import com.openzoosim.redis.SettingsPOJO;
import com.openzoosim.repositories.SettingsRepository;
import com.openzoosim.services.ISettingsService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SettingsServiceImpl implements ISettingsService {

    @Inject
    RedisClient redisClient;

    @Inject
    SettingsRepository settingsRepository;

    @Override
    public SettingsPOJO getCachedSettings() {

        var settings = redisClient.getCachedSettings();

        if (settings != null) {
            return settings;
        }

        var newSettings = this.settingsRepository.getSettings();

        var settingsPOJO = new SettingsPOJO(newSettings.is_email_enabled, newSettings.is_maintenance_mode, newSettings.maintenance_message);

        redisClient.setSettings(settingsPOJO);

        return settingsPOJO;
    }
    
}

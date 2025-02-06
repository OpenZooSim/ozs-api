package com.openzoosim.services;

import com.openzoosim.redis.SettingsPOJO;

public interface ISettingsService {
    SettingsPOJO getCachedSettings();
}

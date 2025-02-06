package com.openzoosim.redis;

public class SettingsPOJO {
    public boolean is_email_enabled;
    public boolean is_maintenance_mode;
    public String maintenance_message;

    public SettingsPOJO(boolean is_email_enabled, boolean is_maintenance_mode, String maintenance_message) {
        this.is_email_enabled = is_email_enabled;
        this.is_maintenance_mode = is_maintenance_mode;
        this.maintenance_message = maintenance_message;
    }

    public SettingsPOJO() {
    }
}

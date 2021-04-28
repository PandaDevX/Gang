package com.redspeaks.gang;

import org.bukkit.plugin.java.JavaPlugin;

public final class GangPlugin extends JavaPlugin {

    private static GangPlugin instance = null;

    @Override
    public void onEnable() {

        instance = this;
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static GangPlugin getInstance() {
        return instance;
    }
}

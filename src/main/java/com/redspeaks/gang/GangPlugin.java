package com.redspeaks.gang;

import com.redspeaks.gang.api.command.AbstractCommand;
import com.redspeaks.gang.commands.GangCommand;
import com.redspeaks.gang.listeners.MiningListener;
import com.redspeaks.gang.listeners.MoneyGangListener;
import com.redspeaks.gang.listeners.TokenListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GangPlugin extends JavaPlugin {

    private static GangPlugin instance = null;
    private Economy econ = null;

    @Override
    public void onEnable() {

        instance = this;
        // Plugin startup logic


        if(!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        AbstractCommand abstractCommand = new GangCommand();
        getCommand(abstractCommand.getInfo().name()).setExecutor(abstractCommand);

        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MoneyGangListener(), this);
        getServer().getPluginManager().registerEvents(new TokenListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Economy getEconomy() {
        return econ;
    }

    public static GangPlugin getInstance() {
        return instance;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}

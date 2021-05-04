package com.redspeaks.gang;

import com.redspeaks.gang.api.command.AbstractCommand;
import com.redspeaks.gang.api.gangs.Storage;
import com.redspeaks.gang.commands.GangCommand;
import com.redspeaks.gang.database.DatabaseManager;
import com.redspeaks.gang.gui.MainGUI;
import com.redspeaks.gang.listeners.GangsListener;
import com.redspeaks.gang.listeners.MiningListener;
import com.redspeaks.gang.listeners.MoneyGangListener;
import com.redspeaks.gang.listeners.TokenListener;
import com.redspeaks.gang.objects.Database;
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

        saveDefaultConfig();

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
        getServer().getPluginManager().registerEvents(new GangsListener(), this);
        getServer().getPluginManager().registerEvents(new MainGUI(), this);

        DatabaseManager databaseManager = getDatabaseManager();
        databaseManager.createTableForGangs();
        databaseManager.loadData();

    }

    @Override
    public void onDisable() {
        if(!Storage.playerDatabase.isEmpty()) {
            getDatabaseManager().saveData(Storage.playerDatabase);
        }

        getDatabaseManager().close();
    }

    public DatabaseManager getDatabaseManager() {
        return new DatabaseManager();
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

    public Database getDatabase() {
        return new Database() {
            @Override
            public String host() {
                return getConfig().getString("MySQL.host");
            }

            @Override
            public String port() {
                return getConfig().getString("MySQL.port");
            }

            @Override
            public String database() {
                return getConfig().getString("MySQL.database");
            }

            @Override
            public String user() {
                return getConfig().getString("MySQL.user");
            }

            @Override
            public String pass() {
                return getConfig().getString("MySQL.pass");
            }
        };
    }
}

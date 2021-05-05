package com.redspeaks.gang;

import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.command.AbstractCommand;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.Storage;
import com.redspeaks.gang.commands.GangCommand;
import com.redspeaks.gang.commands.GangManagerCommand;
import com.redspeaks.gang.database.DatabaseManager;
import com.redspeaks.gang.gui.GangInfo;
import com.redspeaks.gang.gui.MainGUI;
import com.redspeaks.gang.listeners.*;
import com.redspeaks.gang.objects.Database;
import com.redspeaks.gang.objects.Gang;
import com.redspeaks.gang.scoreboard.GangScoreboard;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.AbstractList;

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
        AbstractCommand managerCommand = new GangManagerCommand();
        getCommand(managerCommand.getInfo().name()).setExecutor(managerCommand);

        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MoneyGangListener(), this);
        getServer().getPluginManager().registerEvents(new TokenListener(), this);
        getServer().getPluginManager().registerEvents(new GangsListener(), this);
        getServer().getPluginManager().registerEvents(new MainGUI(), this);
        getServer().getPluginManager().registerEvents(new JoinAndLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new GangInfo(), this);

        DatabaseManager databaseManager = getDatabaseManager();
        databaseManager.createTableForGangs();
        databaseManager.loadData();


        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(!Storage.playerDatabase.isEmpty()) {
                if(!Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.isOp()).forEach(
                            p -> p.sendMessage(ChatUtil.colorize("&c&l(!) &7Attempting to save all the datas"))
                    );
                }
                getServer().getConsoleSender().sendMessage(ChatUtil.colorize("&c&l(!) &7Attempting to save all the datas"));
                getDatabaseManager().saveData(Storage.playerDatabase);
                if(!Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.isOp()).forEach(
                            p -> p.sendMessage(ChatUtil.colorize("&c&l(!) &7Successfully saved to database"))
                    );
                }
                getServer().getConsoleSender().sendMessage(ChatUtil.colorize("&c&l(!) &7Successfully saved to database"));
                Storage.playerDatabase.clear();

                if(!Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.isOp()).forEach(
                            p -> p.sendMessage(ChatUtil.colorize("&c&l(!) &7Attempting to reload all the datas"))
                    );
                }
                getServer().getConsoleSender().sendMessage(ChatUtil.colorize("&c&l(!) &7Attempting to reload all the datas"));
                getDatabaseManager().loadData();
                if(!Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.isOp()).forEach(
                            p -> p.sendMessage(ChatUtil.colorize("&c&l(!) &7Successfully reloaded data from database"))
                    );
                }
                getServer().getConsoleSender().sendMessage(ChatUtil.colorize("&c&l(!) &7Successfully reloaded data from database"));
            }
        }, (30L*60L)*20L, (30*60L)*20L);

    }

    @Override
    public void onDisable() {
        if(!Storage.playerDatabase.isEmpty()) {
            getDatabaseManager().saveData(Storage.playerDatabase);
            Storage.playerDatabase.clear();
        }

        if(!Bukkit.getOnlinePlayers().isEmpty()) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                GangPlayer gangPlayer = Gang.getPlayer(player);
                if(gangPlayer.hasGang()) {
                    Team team = gangPlayer.getGang().getTeam();
                    if(team.hasPlayer(gangPlayer.asOfflinePlayer())) {
                        team.removePlayer(gangPlayer.asOfflinePlayer());
                    }
                }
            }
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

    public GangScoreboard gangScoreboard() {
        return new GangScoreboard();
    }
}

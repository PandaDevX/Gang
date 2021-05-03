package com.redspeaks.gang.api.gangs;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface GangPlayer {

    UUID getUniqueId();

    Player asPlayer();

    boolean hasGang();

    void addToGang(GangType gangType);

    GangType getGang();

    OfflinePlayer asOfflinePlayer();

    int getLevel();

    double getExp();

    void levelUp();

    void addExp(double exp);

    void setExp(double exp);

    double getGoalExp();

    public PlayerData getPlayerData();

    public void setGang(GangType gang);

    boolean isOnline();

    void sendMessage(String message);
}

package com.redspeaks.gang.objects;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.gangs.*;
import com.redspeaks.gang.api.events.GangPlayerExpChangeEvent;
import com.redspeaks.gang.api.events.GangPlayerJoinEvent;
import com.redspeaks.gang.api.events.GangPlayerLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Gang {


    private static final HashMap<UUID, GangPlayer> gangRegistry = new HashMap<>();
    private static final HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();

    public static void rewardPlayer(GangPlayer player) {
        List<Integer> list = player.getGang().getRewards();
        Random random = new Random();
        Integer prize = list.get(random.nextInt(list.size()));
        player.getGang().reward(player, prize);
        random = null;
        list = null;
    }

    public static double getWorth(Material material) {
        if(!material.isBlock()) {
            return 0;
        }
        for(String blockSection : GangPlugin.getInstance().getConfig().getConfigurationSection("Options.Miner Gang.Blocks").getKeys(false)) {
            if(material.name().equals(blockSection)) {
                String path = "Options.Miner Gang.Blocks." + blockSection;
                Random r = new Random();
                int low = ((int)(Double.parseDouble(GangPlugin.getInstance().getConfig().getString(path).split("-")[0])/2));
                int high = ((int) (Double.parseDouble(GangPlugin.getInstance().getConfig().getString(path).split("-")[1])));
                int result = r.nextInt(high-low) + low;
                r = null;
                return result;
            }
        }
        return 0;
    }

    public static void clear() {
        gangRegistry.clear();
        playerDataMap.clear();
    }

    public static GangPlayer getPlayer(OfflinePlayer player) {
        if(gangRegistry.containsKey(player.getUniqueId())) {
            return gangRegistry.get(player.getUniqueId());
        }
        final GangPlayer gangPlayer = new GangPlayer() {
            @Override
            public boolean equals(Object object) {
                if(this == object) {
                    return true;
                }
                if(!(object instanceof GangPlayer)) {
                    return false;
                }
                return getUniqueId().equals(((GangPlayer)object).getUniqueId());
            }

            @Override
            public UUID getUniqueId() {
                return player.getUniqueId();
            }

            @Override
            public Player asPlayer() {
                return player.getPlayer();
            }

            @Override
            public boolean hasGang() {
                if(!Storage.playerDatabase.containsKey(player.getUniqueId().toString())) {
                    return false;
                }
                return getPlayerData().gang() != GangType.UNKNOWN;
            }

            @Override
            public void addToGang(GangType gangType) {
                getPlayerData().setGang(gangType);
            }

            @Override
            public GangType getGang() {
                return getPlayerData().gang();
            }

            @Override
            public OfflinePlayer asOfflinePlayer() {
                return player;
            }

            @Override
            public int getLevel() {
                return getPlayerData().level();
            }

            @Override
            public double getExp() {
                return getPlayerData().exp();
            }

            @Override
            public void levelUp() {
                GangPlayerLevelUpEvent gangPlayerLevelUpEvent = new GangPlayerLevelUpEvent(this, getLevel(), getLevel() + 1);
                if (!Bukkit.isPrimaryThread()) {
                    Bukkit.getScheduler().runTask(GangPlugin.getInstance(), () -> Bukkit.getPluginManager().callEvent(gangPlayerLevelUpEvent));
                } else {
                    Bukkit.getPluginManager().callEvent(gangPlayerLevelUpEvent);
                }
                if(!gangPlayerLevelUpEvent.isCancelled()) {
                    getPlayerData().setLevel(getLevel() + 1);
                }
            }

            @Override
            public void addExp(double exp) {
                if (!Bukkit.isPrimaryThread()) {
                    Bukkit.getScheduler().runTask(GangPlugin.getInstance(), () -> Bukkit.getPluginManager().callEvent(new GangPlayerExpChangeEvent(this, getExp(), getExp() + exp)));
                } else {
                    Bukkit.getPluginManager().callEvent(new GangPlayerExpChangeEvent(this, getExp(), getExp() + exp));
                }
                getPlayerData().setExp(getExp() + exp);
            }

            @Override
            public void setExp(double exp) {
                getPlayerData().setExp(exp);
            }

            @Override
            public double getGoalExp() {
                return ((getGang().getConfigOptionDouble("goal") * (getGang().getConfigOptionDouble("increment")/100D)) * getLevel()) + getGang().getConfigOptionDouble("goal");
            }

            @Override
            public void setGang(GangType type) {
                if (!Bukkit.isPrimaryThread()) {
                    Bukkit.getScheduler().runTask(GangPlugin.getInstance(), () -> Bukkit.getPluginManager().callEvent(new GangPlayerJoinEvent(this, getGang(), type)));
                } else {
                    Bukkit.getPluginManager().callEvent(new GangPlayerJoinEvent(this, getGang(), type));
                }
                getPlayerData().setGang(type);

            }

            @Override
            public void sendTitle(String message) {
                if(player.isOnline()) {
                    player.getPlayer().sendTitle(ChatUtil.colorize(message), "", 10, 70, 20);
                }
            }

            @Override
            public PlayerData getPlayerData() {
                if(playerDataMap.containsKey(getUniqueId())) {
                    return playerDataMap.get(getUniqueId());
                }
                PlayerData playerData = new PlayerData(getUniqueId().toString(),
                        1, 0, GangType.UNKNOWN);
                playerDataMap.put(getUniqueId(), playerData);
                return playerData;
            }

            @Override
            public boolean isOnline() {
                return player.isOnline();
            }

            @Override
            public void sendMessage(String message) {
                if (player.isOnline()) {
                    player.getPlayer().sendMessage(ChatUtil.colorize(message));
                }
            }

            @Override
            public void finalize() {
                try {
                    super.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        };
        gangRegistry.put(player.getUniqueId(), gangPlayer);
        return gangPlayer;
    }

}

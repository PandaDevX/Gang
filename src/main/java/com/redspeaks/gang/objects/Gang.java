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

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Gang {

    public static void rewardPlayer(GangPlayer player) {
        List<Integer> list = player.getGang().getRewards();
        Random random = new Random();
        Integer prize = list.get(random.nextInt(list.size()));
        player.getGang().reward(player, prize);
        random = null;
        list = null;
        return;
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

    public static GangPlayer getPlayer(OfflinePlayer player) {
        final GangPlayer gangPlayer = new GangPlayer() {
            @Override
            public boolean equals(Object object) {
                return player.equals(object);
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
                if(getPlayerData().gang() == null) {
                    return false;
                }
                if(getPlayerData().gang() == GangType.UNKNOWN) {
                    return false;
                }
                return true;
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
                getPlayerData().setLevel(1);
            }

            @Override
            public void sendTitle(String message) {
                player.getPlayer().sendTitle(ChatUtil.colorize(message), "", 10, 70, 20);
            }

            @Override
            public PlayerData getPlayerData() {
                return new PlayerData() {
                    private String[] readData() {
                        if(Storage.playerDatabase.containsKey(player.getUniqueId().toString())) {
                            return Storage.playerDatabase.get(player.getUniqueId().toString()).split("::");
                        }
                        return null;
                    }

                    @Override
                    public int level() {
                        if(readData() == null) {
                            return 1;
                        }
                        try{
                            return Integer.parseInt(readData()[0]);
                        }catch (NumberFormatException e) {
                            return 1;
                        }

                    }

                    @Override
                    public double exp() {
                        if(readData() == null) {
                            return 0;
                        }
                        try {
                            return Double.parseDouble(readData()[1]);
                        }catch (NumberFormatException e) {
                            return 0;
                        }
                    }

                    @Override
                    public GangType gang() {
                        if(readData() == null) {
                            return GangType.UNKNOWN;
                        }
                        return GangType.getGang(readData()[2]);
                    }

                    @Override
                    public void setExp(double exp) {
                        String buildData = level() + "::" + exp + "::" + gang().getPrefix();
                        Storage.playerDatabase.put(player.getUniqueId().toString(), buildData);
                    }

                    @Override
                    public void setLevel(int level) {
                        String buildData = level + "::" + exp() + "::" + gang().getPrefix();
                        Storage.playerDatabase.put(player.getUniqueId().toString(), buildData);
                    }

                    @Override
                    public void setGang(GangType gang) {
                        String buildData = 1 + "::" + 0D + "::" + gang.getPrefix();
                        Storage.playerDatabase.put(player.getUniqueId().toString(), buildData);
                    }
                };
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
        };
        return gangPlayer;
    }

}

package com.redspeaks.gang.objects;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.api.gangs.LeaderBoard;
import com.redspeaks.gang.api.gangs.PlayerData;
import com.redspeaks.gang.api.events.GangPlayerExpChangeEvent;
import com.redspeaks.gang.api.events.GangPlayerJoinEvent;
import com.redspeaks.gang.api.events.GangPlayerLevelUpEvent;
import com.redspeaks.gang.api.gangs.Storage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Gang {

    public static GangPlayer getPlayer(OfflinePlayer player) {
        return new GangPlayer() {
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
                return !getPlayerData().gang().getPrefix().equals("empty");
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
            public void addLevel(int level) {
                if(!Bukkit.isPrimaryThread()) {
                    Bukkit.getScheduler().runTask(GangPlugin.getInstance(), () -> Bukkit.getPluginManager().callEvent(new GangPlayerLevelUpEvent(this, getLevel(), getLevel() + level)));
                } else {
                    Bukkit.getPluginManager().callEvent(new GangPlayerLevelUpEvent(this, getLevel(), getLevel() + level));
                }
                getPlayerData().setLevel(getLevel() + level);
            }

            @Override
            public void addExp(double exp) {
                if(!Bukkit.isPrimaryThread()) {
                    Bukkit.getScheduler().runTask(GangPlugin.getInstance(), () -> Bukkit.getPluginManager().callEvent(new GangPlayerExpChangeEvent(this, getExp(), getExp() + exp)));
                } else {
                    Bukkit.getPluginManager().callEvent(new GangPlayerExpChangeEvent(this, getExp(), getExp() + exp));
                }
                getPlayerData().setExp(getExp() + exp);
            }

            @Override
            public double getGoalExp() {
                if(getGang() == null) {
                    return 0;
                }
                if(getGang() == GangType.UNKNOWN) {
                    return 0;
                }
                if(getGang() == GangType.MINER_GANG) {
                    return Gang.getMineGang().getGoalExp(getLevel());
                }
                return 0;
            }

            @Override
            public void setGang(GangType type) {
                if(hasGang())return;
                if(!Bukkit.isPrimaryThread()) {
                    Bukkit.getScheduler().runTask(GangPlugin.getInstance(), () -> Bukkit.getPluginManager().callEvent(new GangPlayerJoinEvent(this, getGang(), type)));
                } else {
                    Bukkit.getPluginManager().callEvent(new GangPlayerJoinEvent(this, getGang(), type));
                }
                getPlayerData().setGang(type);
            }

            @Override
            public PlayerData getPlayerData() {
                return new PlayerData() {
                    private String[] readData() {
                        return Storage.playerDatabase.get(player.getUniqueId().toString()).split("::");
                    }

                    @Override
                    public int level() {
                        return Integer.parseInt(readData()[0]);
                    }

                    @Override
                    public double exp() {
                        return Integer.parseInt(readData()[1]);
                    }

                    @Override
                    public GangType gang() {
                        return GangType.getGang(readData()[2]);
                    }

                    @Override
                    public void setExp(double exp) {
                        String buildData = level() + "::" + exp + "::" + gang();
                        Storage.playerDatabase.put(player.getUniqueId().toString(), buildData);
                    }

                    @Override
                    public void setLevel(int level) {
                        String buildData = level + "::" + exp() + "::" + gang();
                        Storage.playerDatabase.put(player.getUniqueId().toString(), buildData);
                    }

                    @Override
                    public void setGang(GangType gang) {
                        String buildData = level() + "::" + exp() + "::" + gang.getPrefix();
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
                if(player.isOnline()) {
                    player.getPlayer().sendMessage(ChatUtil.colorize(message));
                }
            }
        };
    }

    public static LeaderBoard getLeaderBoard(GangType gangType) {
        return null;
    }

    public static MineGang getMineGang() {
        return new MineGang();
    }
}

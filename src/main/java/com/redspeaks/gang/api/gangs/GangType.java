package com.redspeaks.gang.api.gangs;

import com.redspeaks.gang.GangPlugin;
import me.revils.enchants.api.PublicRevAPI;

import java.util.List;

public enum GangType {

    MINER_GANG("miner", "Miner Gang"),
    TOKEN_GANG("token", "Token Gang"),
    MONEY_GANG("money", "Money Gang"),
    UNKNOWN("empty", "No Gang");

    private String text;
    private String name;
    GangType(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getPrefix() {
        return this.text;
    }

    public String getName() {
        return this.name;
    }

    public double getConfigOptionDouble(String path) {
        return GangPlugin.getInstance().getConfig().getDouble("Options." + name + "." + path);
    }

    public List<Integer> getRewards() {
        return GangPlugin.getInstance().getConfig().getIntegerList("Options." + name + ".rewards");
    }

    public void reward(GangPlayer player, Integer number) {
        if(this.text.equals("money")) {
            GangPlugin.getInstance().getEconomy().depositPlayer(player.asOfflinePlayer(), number);
        } else if(this.text.equals("token")) {
            PublicRevAPI.addTokens(player.asPlayer(), (long)number);
        } else {
            // TODO miner
        }
    }

    public static GangType getGang(String prefix) {
        for(GangType gangType : GangType.values()) {
            if(gangType.getPrefix().equalsIgnoreCase(prefix)) {
                return gangType;
            }
        }
        return null;
    }

}

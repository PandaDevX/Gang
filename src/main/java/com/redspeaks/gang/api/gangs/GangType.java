package com.redspeaks.gang.api.gangs;

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

    public static GangType getGang(String prefix) {
        for(GangType gangType : GangType.values()) {
            if(gangType.getPrefix().equalsIgnoreCase(prefix)) {
                return gangType;
            }
        }
        return null;
    }

}

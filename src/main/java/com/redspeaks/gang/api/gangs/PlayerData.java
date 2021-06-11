package com.redspeaks.gang.api.gangs;

public class PlayerData implements Comparable<PlayerData> {

    private int level;
    private double exp;
    private GangType type;
    private final String uuid;
    private final int hash;
    public PlayerData(String uuid, int level, double exp, GangType type) {
        this.level = level;
        this.exp = exp;
        this.type = type;
        this.uuid = uuid;
        this.hash = uuid.hashCode();
    }

    public String getUniqueId() {
        return uuid;
    }

    public int level() {
        return level;
    }

    public double exp() {
        return exp;
    }

    public GangType gang() {
        return type;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public void setGang(GangType type) {
        this.type = type;
    }

    @Override
    public int compareTo(PlayerData playerData) {
        int compare = Integer.compare(level(), playerData.level());
        if(compare == 0) {
            compare = Double.compare(exp(), playerData.exp());
        }
        return compare;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof PlayerData)) {
            return false;
        }
        PlayerData otherData = (PlayerData) obj;
        return getUniqueId().equals(otherData.getUniqueId());
    }

    @Override
    public int hashCode() {
        return hash;
    }

}

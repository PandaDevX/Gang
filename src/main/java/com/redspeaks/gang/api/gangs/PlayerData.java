package com.redspeaks.gang.api.gangs;

public interface PlayerData {

    int level();

    double exp();

    GangType gang();

    void setExp(double exp);

    void setLevel(int level);

    void setGang(GangType gang);
}

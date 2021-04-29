package com.redspeaks.gang.objects;

import com.redspeaks.gang.api.gangs.*;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MoneyGang implements GangBase {

    @Override
    public LeaderBoard getLeaderBoard() {
        return Gang.getLeaderBoard(GangType.MINER_GANG);
    }

    @Override
    public double getGoalExp(int level) {
        return 0;
    }

    @Override
    public List<String> getMembers() {
        return Storage.playerDatabase.keySet().stream().map(c-> c.split("::")[0]).filter(c -> Gang.getPlayer(Bukkit.getOfflinePlayer(UUID.fromString(c))).getGang() == GangType.MINER_GANG).collect(Collectors.toList());
    }

    @Override
    public void addMember(GangPlayer gangPlayer) {
        gangPlayer.setGang(GangType.MONEY_GANG);
    }

    @Override
    public void kickMember(GangPlayer gangPlayer) {
        gangPlayer.setGang(GangType.UNKNOWN);
    }

    @Override
    public boolean isMember(GangPlayer gangPlayer) {
        return getMembers().contains(gangPlayer.getUniqueId().toString());
    }

    @Override
    public boolean isLevelExist(int level) {
        return false;
    }
}

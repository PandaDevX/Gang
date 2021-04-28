package com.redspeaks.gang.objects;

import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.api.gangs.LeaderBoard;
import com.redspeaks.gang.api.gangs.GangBase;
import com.redspeaks.gang.api.gangs.Storage;
import org.bukkit.Bukkit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MineGang implements GangBase {

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
        return Storage.playerDatabase.keySet().stream().filter(uuid -> Gang.getPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid))).getPlayerData().gang() == GangType.MINER_GANG).collect(Collectors.toList());
    }

    @Override
    public void addMember(GangPlayer gangPlayer) {
        gangPlayer.setGang(GangType.MINER_GANG);
    }

    @Override
    public void kickMember(GangPlayer gangPlayer) {
        gangPlayer.setGang(GangType.UNKNOWN);
    }

    @Override
    public boolean isMember(GangPlayer gangPlayer) {
        return false;
    }
}

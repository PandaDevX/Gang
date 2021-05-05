package com.redspeaks.gang.scoreboard;

import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GangScoreboard {

    static Scoreboard scoreboard = null;
    Team miner = null;
    Team money = null;
    Team token = null;

    static {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public Scoreboard getMainScoreboard() {
        return scoreboard;
    }

    public void init() {
        this.miner = getMainScoreboard().registerNewTeam("Miner");
        this.money = getMainScoreboard().registerNewTeam("Money");
        this.token = getMainScoreboard().registerNewTeam("Token");

        Arrays.stream(GangType.values()).filter(g -> !g.getPrefix().equals("empty")).forEach(g -> g.getTeam().setPrefix(
                ChatUtil.colorize(g.getNameTagPrefix()[0])
        ));
        Arrays.stream(GangType.values()).filter(g -> !g.getPrefix().equals("empty")).forEach(g -> g.getTeam().setColor(
                ChatColor.getByChar(g.getNameTagPrefix()[1].replace("&", ""))
        ));
        if(!Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.getOnlinePlayers().stream().filter(p -> Gang.getPlayer(p).hasGang()).forEach(p -> Gang.getPlayer(p).getGang().getTeam().addPlayer(p));
        }
    }

    public Team getTeamMiner() {
        return miner;
    }

    public Team getTeamMoney() {
        return money;
    }

    public Team getTeamToken() {
        return token;
    }
}

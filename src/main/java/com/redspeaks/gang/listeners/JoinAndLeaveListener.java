package com.redspeaks.gang.listeners;

import com.redspeaks.gang.api.events.GangPlayerJoinEvent;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;

public class JoinAndLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GangPlayer player = Gang.getPlayer(e.getPlayer());
        if(player.hasGang()) {
            player.getGang().getTeam().addPlayer(player.asOfflinePlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        GangPlayer player = Gang.getPlayer(e.getPlayer());
        if(player.hasGang()) {
            Team team = player.getGang().getTeam();
            if(team.hasPlayer(player.asOfflinePlayer())) {
                team.removePlayer(player.asOfflinePlayer());
            }
        }
    }

    @EventHandler
    public void onGangJoin(GangPlayerJoinEvent e) {
        if(e.getPlayer().hasGang()) {
            Team team = e.getPlayer().getGang().getTeam();
            if(team.hasPlayer(e.getPlayer().asOfflinePlayer())) {
                team.removePlayer(e.getPlayer().asOfflinePlayer());
            }
        }
    }
}

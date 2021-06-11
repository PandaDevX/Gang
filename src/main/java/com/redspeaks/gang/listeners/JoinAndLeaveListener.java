package com.redspeaks.gang.listeners;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.events.GangPlayerJoinEvent;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.api.gangs.Storage;
import com.redspeaks.gang.database.DatabaseManager;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinAndLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GangPlayer player = Gang.getPlayer(e.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                ChatUtil.reloadNameTag(player);
            }
        }.runTask(GangPlugin.getInstance());

        GangPlugin.getInstance().getDatabaseManager().loadData(e.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        GangPlugin.getInstance().getDatabaseManager().saveData(Storage.playerDatabase, e.getPlayer());
    }

    @EventHandler
    public void onGangJoin(GangPlayerJoinEvent e) {
        if(e.getTo() == GangType.UNKNOWN) return;
        GangPlayer player = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                ChatUtil.reloadNameTag(player);
            }
        }.runTask(GangPlugin.getInstance());

        player.sendTitle(GangPlugin.getInstance().getConfig().getString("Join Message.title")
        .replace("{gang}", e.getTo().getName()));
        if(!GangPlugin.getInstance().getConfig().getString("Join Message.broadcast").equals("")) {
            if(!Bukkit.getOnlinePlayers().isEmpty()) {
                new BukkitRunnable() {
                    public void run() {
                        Bukkit.getOnlinePlayers().stream().filter(p -> Gang.getPlayer(p).hasGang()).filter(p -> Gang.getPlayer(p).getGang() == e.getTo())
                                .forEach(p -> Gang.getPlayer(p).sendMessage(GangPlugin.getInstance().getConfig().getString("Join Message.broadcast")
                                        .replace("{player}", player.asOfflinePlayer().getName()).replace("{gang}", e.getTo().getName())));
                    }
                }.runTask(GangPlugin.getInstance());
            }
        }
    }
}

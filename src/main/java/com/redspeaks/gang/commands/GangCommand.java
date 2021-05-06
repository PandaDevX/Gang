package com.redspeaks.gang.commands;

import com.redspeaks.gang.api.command.AbstractCommand;
import com.redspeaks.gang.api.command.CommandInfo;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.api.gangs.LeaderBoard;
import com.redspeaks.gang.gui.GangInfo;
import com.redspeaks.gang.gui.MainGUI;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandInfo(name = "gang", permission = "gang.player", requiresPlayer = true)
public class GangCommand extends AbstractCommand {

    @Override
    public void execute(Player player, String[] args) {
        GangPlayer gangPlayer = Gang.getPlayer(player);
        if(args.length == 0) {
            if(!gangPlayer.hasGang()) {
                MainGUI mainGUI = new MainGUI();
                mainGUI.openInventory(gangPlayer.asPlayer());
                mainGUI = null;
                return;
            }
            GangInfo gangInfo = new GangInfo();
            gangInfo.openInventory(gangPlayer.asPlayer());
            gangInfo = null;
            return;
        }

        if(args[0].equalsIgnoreCase("admin")) {
            if(!gangPlayer.asPlayer().hasPermission("gang.admin")) {
                gangPlayer.sendMessage("&7Insufficient permission");
                return;
            }
            MainGUI mainGUI = new MainGUI();
            mainGUI.openInventory(gangPlayer.asPlayer());
            mainGUI = null;
            return;
        }
        if(args[0].equalsIgnoreCase("set")) {
            if(!player.hasPermission("gang.admin")) {
                sendMessage(player, "&7Insufficient permission");
                return;
            }
            if(args.length != 3) {
                gangPlayer.sendMessage("&7Correct argument: &c/gang set <player> <gang>");
                return;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if(!target.hasPlayedBefore()) {
                gangPlayer.sendMessage("&7Player &c" + target.getName() + " &7haven't played the server before");
                return;
            }
            GangPlayer targetPlayer = Gang.getPlayer(target);
            targetPlayer.setGang(GangType.getGang(args[2]));
            gangPlayer.sendMessage("&7Successfully changed player: &c" + target.getName() + "'s &7gang to: &c" + GangType.getGang(args[2]).getName());
            return;
        }
        if(args[0].equalsIgnoreCase("info")) {
            if(!player.hasPermission("gang.admin")) {
                sendMessage(player, "&7Insufficient permission");
                return;
            }
            if(args.length != 2) {
                gangPlayer.sendMessage("&7Correct argument: &c/gang info <player>");
                return;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if(!target.hasPlayedBefore()) {
                gangPlayer.sendMessage("&7Player &c" + target.getName() + " &7haven't played the server before");
                return;
            }
            GangPlayer targetPlayer = Gang.getPlayer(target);
            GangInfo gangInfo = new GangInfo();
            gangInfo.openInventory(targetPlayer.asPlayer(), player);
            gangInfo = null;
            return;
        }
        if(args[0].equalsIgnoreCase("leaderboard")) {
            if(!gangPlayer.hasGang()) {
                gangPlayer.sendMessage("&7You must be in a gang to do that");
                return;
            }
            if(args.length == 1) {
                LeaderBoard leaderBoard = Gang.getPlayer(player).getGang().getLeaderBoard();
                leaderBoard.showTo(Gang.getPlayer(player));
                return;
            }
            if(!player.hasPermission("gang.admin")) {
                sendMessage(player, "&7Insufficient permission");
                return;
            }
            GangType gangType = GangType.getGang(args[1]);
            if(gangType == null) {
                gangPlayer.sendMessage("&7Unknown gang");
                return;
            }
            LeaderBoard leaderBoard = gangType.getLeaderBoard();
            leaderBoard.showTo(gangPlayer);
            return;
        }

        gangPlayer.sendMessage("&7Unknown command");

    }
}

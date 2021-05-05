package com.redspeaks.gang.commands;

import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.command.AbstractCommand;
import com.redspeaks.gang.api.command.CommandInfo;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "gangman", permission = "gang.admin")
public class GangManagerCommand extends AbstractCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 4) {
            sendMessage(sender, "&7Correct arguments: &c/gangman <exp/level> <player> <set/add> <amount>");
            return;
        }
        String type = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if(!target.hasPlayedBefore()) {
            sendMessage(sender, "&7That player never played the server before");
            return;
        }
        String process = args[2];
        if(!ChatUtil.isInt(args[3])) {
            sendMessage(sender, "&7Please type a correct amount");
            return;
        }
        int amount = Integer.parseInt(args[3]);
        if(amount <= 0) {
            sendMessage(sender, "&7Choose any positive numbers");
            return;
        }
        GangPlayer targetPlayer = Gang.getPlayer(target);
        switch (type.toLowerCase()) {
            case "exp":
                if(process.equalsIgnoreCase("set")) {
                    sendMessage(sender, "&7Successfully setted the exp of player &c" + args[1] + " &7Amount: &f" + amount);
                    targetPlayer.setExp(amount);
                    break;
                }
                if(process.equalsIgnoreCase("add")) {
                    sendMessage(sender, "&7Successfully added exp to player &c" + args[1] + " &7Amount: &f" + amount);
                    targetPlayer.addExp(amount);
                    break;
                }
                sendMessage(sender, "&7Unknown command");
                break;
            case "level":
                if(process.equalsIgnoreCase("set")) {
                    sendMessage(sender, "&7Successfully setted the level of player &c" + args[1] + " &7Amount: &f" + amount);
                    targetPlayer.getPlayerData().setLevel(amount);
                    break;
                }
                if(process.equalsIgnoreCase("add")) {
                    sendMessage(sender, "&7Successfully added level to player &c" + args[1] + " &7Amount: &f" + amount);
                    targetPlayer.getPlayerData().setLevel(targetPlayer.getLevel() + amount);
                    break;
                }
                sendMessage(sender, "&7Unknown command");
                break;
            default:
                sendMessage(sender, "&7Unknown command");
                break;
        }
        return;
    }
}

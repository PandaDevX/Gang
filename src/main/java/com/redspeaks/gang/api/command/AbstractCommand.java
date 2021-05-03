package com.redspeaks.gang.api.command;

import com.redspeaks.gang.api.chat.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements CommandExecutor {
    private final CommandInfo info;

    public AbstractCommand() {
        info = getClass().getDeclaredAnnotation(CommandInfo.class);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!hasPermission(sender, info.permission())) {
            sendMessage(sender, "&7Insufficient permission");
            return true;
        }

        if(info.requiresPlayer()) {
            if(!(sender instanceof Player)) {
                sendMessage(sender, "&7You must be a player to do that");
                return true;
            }
            execute((Player)sender, args);
            return true;
        }
        execute(sender, args);
        return false;
    }

    public CommandInfo getInfo() {
        return info;
    }

    public void execute(CommandSender sender, String[] args) {}
    public void execute(Player player, String[] args) {}

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatUtil.colorize(message));
    }

    public void sendMessage(CommandSender sender, String... messages) {
        for(String message : messages) {
            sendMessage(sender, message);
        }
    }

    public void sendMessage(CommandSender sender, String node, String message) {
        if(sender.hasPermission(node)) {
            sendMessage(sender, message);
        }
    }

    public boolean hasPermission(CommandSender sender, String node) {
        return sender.hasPermission(node);
    }
}

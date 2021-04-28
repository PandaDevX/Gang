package com.redspeaks.gang.api.chat;

import org.bukkit.ChatColor;

public class ChatUtil {

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String stripColor(String text) {
        return ChatColor.stripColor(text);
    }
}

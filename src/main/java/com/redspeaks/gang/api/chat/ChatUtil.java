package com.redspeaks.gang.api.chat;

import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.List;

public class ChatUtil {

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String stripColor(String text) {
        return ChatColor.stripColor(text);
    }

    public static List<String> colorizeList(List<String> list) {
        for(int i = 0; i < list.size(); i++) {
            list.set(i, colorize(list.get(i)));
        }
        return list;
    }

    public static String formatNumber(double number)  {
       DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
       return decimalFormat.format(number);
    }

    public static boolean isInt(String text) {
        try {
            Integer.parseInt(text);
        }catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}

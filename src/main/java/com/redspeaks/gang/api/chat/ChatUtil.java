package com.redspeaks.gang.api.chat;

import com.redspeaks.gang.api.gangs.GangPlayer;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.*;

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

    public static Integer getHighestValue(List<Integer> list) {
        Collections.reverse(list);
        return list.get(0);
    }

    public static Integer getHighestValue(List<Integer> list, int number) {
        Collections.reverse(list);
        return list.get(number);
    }

    public static int parseInt(String text) {
        return Integer.parseInt(text);
    }

    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static void reloadNameTag(GangPlayer gangPlayer) {
        if(gangPlayer.hasGang()) {
            String[] nameTag = gangPlayer.getGang().getNameTagPrefix();

            Scoreboard scoreboard = gangPlayer.asPlayer().getScoreboard();

            Team team = scoreboard.getTeam(gangPlayer.getGang().getPrefix()) == null ? scoreboard.registerNewTeam(gangPlayer.getGang().getPrefix()) : scoreboard.getTeam(gangPlayer.getGang().getPrefix());
            team.setPrefix(ChatUtil.colorize(nameTag[0] + " "));
            try {
                team.setColor(ChatColor.getByChar(nameTag[1].replace("&", "")));
            }catch (ArrayIndexOutOfBoundsException e) {
                team.setColor(ChatColor.GRAY);
            }
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
            team.addPlayer(gangPlayer.asOfflinePlayer());
        }
    }
}

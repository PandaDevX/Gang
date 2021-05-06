package com.redspeaks.gang.api.gangs;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.gui.GUIOptions;
import com.redspeaks.gang.objects.Gang;
import me.revils.enchants.api.PublicRevAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public enum GangType {

    MINER_GANG("miner", "Miner Gang"),
    TOKEN_GANG("token", "Token Gang"),
    MONEY_GANG("money", "Money Gang"),
    UNKNOWN("empty", "No Gang");

    private String text;
    private String name;
    GangType(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getPrefix() {
        return this.text;
    }

    public String getName() {
        return this.name;
    }

    public double getConfigOptionDouble(String path) {
        return GangPlugin.getInstance().getConfig().getDouble("Options." + name + "." + path);
    }

    public List<Integer> getRewards() {
        return GangPlugin.getInstance().getConfig().getIntegerList("Options." + name + ".rewards");
    }

    public String[] getNameTagPrefix() {
        return GangPlugin.getInstance().getConfig().getString("Options." + name + ".prefix").split(" ");
    }

    public void reward(GangPlayer player, Integer number) {
        if(this.text.equals("money")) {
            GangPlugin.getInstance().getEconomy().depositPlayer(player.asOfflinePlayer(), number);
        } else if(this.text.equals("token")) {
            PublicRevAPI.addTokens(player.asPlayer(), (long)number);
        } else {
            Random random = new Random();
            PublicRevAPI.setREnchantLevel(player.asPlayer(), number, random.nextInt(100));
            random = null;
        }
    }

    public LeaderBoard getLeaderBoard() {
        final LeaderBoard leaderBoard = new LeaderBoard() {
            @Override
            public void showTo(GangPlayer gangPlayer) {
                if (Storage.playerDatabase.isEmpty()) {
                    gangPlayer.sendMessage("&7Leaderboards are empty");
                    return;
                }
                List<String> members = Storage.playerDatabase.keySet().stream().filter(
                        t -> Storage.playerDatabase.get(t).split("::")[2].equals(getPrefix())
                ).collect(Collectors.toList());
                List<Integer> levels = new ArrayList<>();
                for (String member : members) {
                    int level = Integer.parseInt(Storage.playerDatabase.get(member).split("::")[0]);
                    levels.add(level);
                }
                int index = 0;
                int highestLevel = ChatUtil.getHighestValue(levels, index);
                List<String> membersHighest = members.stream().filter(
                        t -> (ChatUtil.parseInt(Storage.playerDatabase.get(t).split("::")[0]) == highestLevel)
                ).collect(Collectors.toList());

                while (membersHighest.size() > members.size()) {
                    int anotherLevel = ChatUtil.getHighestValue(levels, ++index);
                    members.addAll(members.stream().filter(
                            t -> (ChatUtil.parseInt(Storage.playerDatabase.get(t).split("::")[0]) == anotherLevel)
                    ).collect(Collectors.toList()));
                }


                HashMap<String, Double> playerExps = new HashMap<>();
                membersHighest.forEach(
                        t -> playerExps.put(t, Double.parseDouble(Storage.playerDatabase.get(t).split("::")[1]))
                );
                HashMap<String, Double> sortedMap = ChatUtil.sortByValue(playerExps);
                List<String> top9 = sortedMap.keySet().stream().limit(9).collect(Collectors.toList());
                int[] slotsToFill = {4, 12, 13, 14, 20, 21, 22, 23, 24};
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, ChatUtil.colorize("&eLeaderboard"));
                ItemStack itemStack = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(" ");
                itemStack.setItemMeta(itemMeta);
                for (int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, itemStack);
                }
                for (int i = 0; i < 9; i++) {
                    if(top9.size() <= i) {
                        continue;
                    }
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(top9.get(i)));
                    ItemStack head = GUIOptions.getSkull(player, "&bTop &c&l" + (i+1), "", "&6&l&nName:&c&l ( " + player.getName() + " )", "&6&l&nLevel:&c&l ( " + Gang.getPlayer(player).getLevel() + " )", "&6&l&nExp:&c&l ( " + Gang.getPlayer(player).getExp() + " )");
                    inventory.setItem(slotsToFill[i], head);
                }
                gangPlayer.asPlayer().openInventory(inventory);
            }

            @Override
            public void showTo(Player player) {
                if (Storage.playerDatabase.isEmpty()) {
                    player.sendMessage(ChatUtil.colorize("&7Leaderboards are empty"));
                    return;
                }
                List<String> members = Storage.playerDatabase.keySet().stream().filter(
                        t -> Storage.playerDatabase.get(t).split("::")[2].equals(getPrefix())
                ).collect(Collectors.toList());
                List<Integer> levels = new ArrayList<>();
                for (String member : members) {
                    int level = Integer.parseInt(Storage.playerDatabase.get(member).split("::")[0]);
                    levels.add(level);
                }
                int highestLevel = ChatUtil.getHighestValue(levels);
                List<String> membersHighest = members.stream().filter(
                        t -> (ChatUtil.parseInt(Storage.playerDatabase.get(t).split("::")[0]) == highestLevel)
                ).collect(Collectors.toList());


                HashMap<String, Double> playerExps = new HashMap<>();
                membersHighest.forEach(
                        t -> playerExps.put(t, Double.parseDouble(Storage.playerDatabase.get(t).split("::")[1]))
                );
                HashMap<String, Double> sortedMap = ChatUtil.sortByValue(playerExps);
                List<String> top9 = sortedMap.keySet().stream().limit(9).collect(Collectors.toList());
                int[] slotsToFill = {4, 12, 13, 14, 20, 21, 22, 23, 24};
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, ChatUtil.colorize("&eLeaderboard"));
                ItemStack itemStack = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(" ");
                itemStack.setItemMeta(itemMeta);
                for (int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, itemStack);
                }
                for (int i = 0; i < 9; i++) {
                    if (top9.get(i) == null)
                        continue;
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(top9.get(i)));
                    ItemStack head = GUIOptions.getSkull(offlinePlayer, "&b" + offlinePlayer.getName(), "", "&6&l&nLevel:&2 " + Gang.getPlayer(offlinePlayer).getLevel(), "&6&l&nExp:&2 " + Gang.getPlayer(offlinePlayer).getExp());
                    inventory.setItem(slotsToFill[i], head);
                }
                player.openInventory(inventory);
            }
        };
        return leaderBoard;
    }

    public static GangType getGang(String prefix) {
        if(prefix.equalsIgnoreCase("empty") || prefix.equalsIgnoreCase("No gang")) {
            return null;
        }
        for(GangType gangType : GangType.values()) {
            if(gangType.getPrefix().equalsIgnoreCase(prefix) || gangType.getName().equalsIgnoreCase(prefix)) {
                return gangType;
            }
        }
        return null;
    }

    public static void removeAllFromTeam() {
        for(GangType gangType : GangType.values()) {
            if(gangType == GangType.UNKNOWN) {
                continue;
            }
            if(!Bukkit.getOnlinePlayers().isEmpty()) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    Team team = player.getScoreboard().getTeam(gangType.getPrefix());
                    if(team != null) {
                        team.unregister();
                    }
                }
            }
        }
    }

}

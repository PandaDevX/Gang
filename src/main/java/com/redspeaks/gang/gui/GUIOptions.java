package com.redspeaks.gang.gui;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUIOptions {

    public static String getOptions(GangType gangType, String options) {
        return GangPlugin.getInstance().getConfig().getString("GUI." + gangType.getName() + "." + options);
    }

    public static List<String> getDescription(GangType gangType, GangPlayer player) {
        List<String> list = GangPlugin.getInstance().getConfig().getStringList("GUI." + gangType.getName() + ".desc");
        for(int i = 0; i < list.size(); i++) {
             list.set(i, list.get(i).replace("{exp}", String.format("%.2f", player.getExp())));
             list.set(i, list.get(i).replace("{goal}", String.format("%.2f", player.getGoalExp())));
        }
        return list;
    }

    public static ItemStack getItemStack(GangType gangType) {
        return new ItemStack(Material.valueOf(getOptions(gangType, "type").toUpperCase()));
    }

    public static ItemStack getSkull(OfflinePlayer player, String displayName) {

        boolean isNewVersion = Arrays.stream(Material.values())
                .map(Material::name)
                .collect(Collectors.toList())
                .contains("PLAYER_HEAD");

        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

        ItemStack item = new ItemStack(type);

        if(!isNewVersion) {
            item.setDurability((short) 3);
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(isNewVersion) {
            meta.setOwningPlayer(player);
        }else {
            meta.setOwner(player.getName());
        }
        meta.setDisplayName(ChatUtil.colorize(displayName));

        item.setItemMeta(meta);

        return item;
    }
}

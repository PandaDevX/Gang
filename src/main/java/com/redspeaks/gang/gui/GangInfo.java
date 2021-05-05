package com.redspeaks.gang.gui;

import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GangInfo implements Listener {

    Inventory inventory = null;

    public void init(GangPlayer gangPlayer) {
        inventory = Bukkit.createInventory(null, 36, ChatUtil.colorize("&eGang Info"));
        ItemStack placeholder = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta placeholderMeta = placeholder.getItemMeta();
        placeholderMeta.setDisplayName(" ");
        placeholder.setItemMeta(placeholderMeta);

        for(int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i , placeholder);
        }

        ItemStack gang = GUIOptions.getItemStack(gangPlayer.getGang());
        ItemMeta gangMeta = gang.getItemMeta();
        gangMeta.setDisplayName(ChatUtil.colorize(GUIOptions.getOptions(gangPlayer.getGang(), "name")));
        gangMeta.setLore(ChatUtil.colorizeList(GUIOptions.getDescription(gangPlayer.getGang(), gangPlayer)));
        gang.setItemMeta(gangMeta);


        inventory.setItem(4, GUIOptions.getSkull(gangPlayer.asOfflinePlayer(), "&b" + gangPlayer.asOfflinePlayer().getName()));
        inventory.setItem(22, gang);
    }

    public void openInventory(Player player) {
        init(Gang.getPlayer(player));
        player.openInventory(inventory);
    }

    public void openInventory(Player player, Player player2) {
        init(Gang.getPlayer(player));
        player2.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().equals(e.getWhoClicked().getInventory()))return;
        if(ChatUtil.stripColor(e.getView().getTitle()).equals("Gang Info")) return;
        e.setCancelled(true);
    }
}

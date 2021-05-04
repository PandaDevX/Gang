package com.redspeaks.gang.gui;

import com.redspeaks.gang.api.chat.ChatUtil;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
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

public class MainGUI implements Listener {

    Inventory inventory = null;

    public void init(GangPlayer player) {
        inventory = Bukkit.createInventory(null, 36, ChatUtil.colorize("&eGang Menu"));
        ItemStack placeholder = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta placeholderMeta = placeholder.getItemMeta();
        placeholder.setItemMeta(placeholderMeta);


        for(int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i , placeholder);
        }

        ItemStack minerGang = GUIOptions.getItemStack(GangType.MINER_GANG);
        ItemMeta minerGangMeta = minerGang.getItemMeta();
        minerGangMeta.setDisplayName(ChatUtil.colorize(GUIOptions.getOptions(GangType.MINER_GANG, "name")));
        minerGangMeta.setLore(ChatUtil.colorizeList(GUIOptions.getDescription(GangType.MINER_GANG, player)));
        minerGang.setItemMeta(minerGangMeta);

        ItemStack moneyGang = GUIOptions.getItemStack(GangType.MONEY_GANG);
        ItemMeta moneyGangMeta = moneyGang.getItemMeta();
        moneyGangMeta.setDisplayName(ChatUtil.colorize(GUIOptions.getOptions(GangType.MONEY_GANG, "name")));
        moneyGangMeta.setLore(ChatUtil.colorizeList(GUIOptions.getDescription(GangType.MONEY_GANG, player)));
        moneyGang.setItemMeta(moneyGangMeta);


        ItemStack tokenGang = GUIOptions.getItemStack(GangType.TOKEN_GANG);
        ItemMeta tokenGangMeta = tokenGang.getItemMeta();
        tokenGangMeta.setDisplayName(ChatUtil.colorize(GUIOptions.getOptions(GangType.TOKEN_GANG, "name")));
        tokenGangMeta.setLore(ChatUtil.colorizeList(GUIOptions.getDescription(GangType.TOKEN_GANG, player)));
        tokenGang.setItemMeta(tokenGangMeta);


        inventory.setItem(4, GUIOptions.getSkull(player.asOfflinePlayer(), "&b" + player.asOfflinePlayer().getName()));
        inventory.setItem(19, minerGang);
        inventory.setItem(22, tokenGang);
        inventory.setItem(25, moneyGang);

    }

    public void openInventory(Player player) {
        init(Gang.getPlayer(player));
        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory() == e.getWhoClicked().getInventory()) return;
        if(!ChatUtil.stripColor(e.getView().getTitle()).equals("Gang Menu")) return;
        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;
        if(!e.getCurrentItem().getItemMeta().hasDisplayName()) return;
        if(ChatUtil.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals("Miner Gang")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            GangPlayer gangPlayer = Gang.getPlayer(player);
            gangPlayer.setGang(GangType.MINER_GANG);
            player.closeInventory();
            gangPlayer.sendMessage("&7Successfully joined &bMiner Gang");
            return;
        }
        if(ChatUtil.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals("Token Gang")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            GangPlayer gangPlayer = Gang.getPlayer(player);
            gangPlayer.setGang(GangType.TOKEN_GANG);
            player.closeInventory();
            gangPlayer.sendMessage("&7Successfully joined &dToken Gang");
            return;
        }
        if(ChatUtil.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals("Money Gang")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            GangPlayer gangPlayer = Gang.getPlayer(player);
            gangPlayer.setGang(GangType.MONEY_GANG);
            player.closeInventory();
            gangPlayer.sendMessage("&7Successfully joined &aMoney Gang");
            return;
        }
        e.setCancelled(true);
    }
}

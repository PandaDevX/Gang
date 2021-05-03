package com.redspeaks.gang.listeners;

import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MiningListener implements Listener {

    @EventHandler
    public void onMining(BlockBreakEvent e) {
        GangPlayer gangPlayer = Gang.getPlayer(e.getPlayer());
        if(!gangPlayer.hasGang()) return;
        if(gangPlayer.getGang() != GangType.MINER_GANG) return;

        final int worth = (int)Gang.getWorth(e.getBlock().getType());
        if(worth != 0) {
           gangPlayer.addExp(worth);
        }
    }
}

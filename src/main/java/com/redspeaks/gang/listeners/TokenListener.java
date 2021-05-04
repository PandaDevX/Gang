package com.redspeaks.gang.listeners;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.objects.Gang;
import me.revils.enchants.CustomEvents.RevPlayerReceiveTokensEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

public class TokenListener implements Listener {


    public void onReceiveTokens(RevPlayerReceiveTokensEvent e) {
        GangPlayer gangPlayer = Gang.getPlayer(e.getPlayer());
        if(!gangPlayer.hasGang()) return;
        if(gangPlayer.getGang() != GangType.TOKEN_GANG) return;

        long received = e.getReceivedTokens();

        int level = gangPlayer.getLevel();

        double gain = level * (received * (GangType.TOKEN_GANG.getConfigOptionDouble("exp") / 100L));

        Random r = new Random();
        int low = ((int)(gain/2));
        int high = (int)gain;
        int result = r.nextInt(high-low) + low;
        r = null;

        gangPlayer.addExp(result);

        double profit = (received * (GangType.MONEY_GANG.getConfigOptionDouble("profit")/100D));

        GangPlugin.getInstance().getEconomy().depositPlayer(gangPlayer.asOfflinePlayer(), profit);
    }
}

package com.redspeaks.gang.listeners;

import com.redspeaks.gang.GangPlugin;
import com.redspeaks.gang.api.events.GangPlayerExpChangeEvent;
import com.redspeaks.gang.api.events.GangPlayerLevelUpEvent;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.objects.Gang;
import me.revils.enchants.api.PublicRevAPI;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.Random;

public class MoneyGangListener implements Listener {

    @EventHandler
    public void onEarn(UserBalanceUpdateEvent e) {
        GangPlayer gangPlayer = Gang.getPlayer(e.getPlayer());
        if(!gangPlayer.hasGang()) return;
        if(gangPlayer.getGang() != GangType.MONEY_GANG) return;

        BigDecimal change = e.getNewBalance().subtract(e.getOldBalance());

        // player level
        int level = gangPlayer.getLevel();

        // player gain exp (level * 20% of earned money)
        double gain = level * (change.doubleValue() * (GangType.MONEY_GANG.getConfigOptionDouble("exp") / 100D));

        Random r = new Random();
        int low = ((int)(gain/2));
        int high = (int)gain;
        int result = r.nextInt(high-low) + low;
        r = null;

        // total exp gained
        gangPlayer.addExp(result);

        // profit
        double profit = (change.doubleValue() * (GangType.MONEY_GANG.getConfigOptionDouble("profit")/100D));
        GangPlugin.getInstance().getEconomy().depositPlayer(gangPlayer.asOfflinePlayer(), profit);
    }

}

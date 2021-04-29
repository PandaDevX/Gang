package com.redspeaks.gang.listeners;

import com.redspeaks.gang.api.events.GangPlayerLevelUpEvent;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.objects.Gang;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.math.BigDecimal;

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
        double gain = level * (change.doubleValue() * .20D);

        // total exp gained
        double exp = gangPlayer.getExp() + gain;

        // level up
        if(exp >= Gang.getMoneyGang().getGoalExp(level)) {
            gangPlayer.addLevel(1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelUp(GangPlayerLevelUpEvent e) {
        if(e.getGangBase().isLevelExist(e.getTo())) return;
        e.setCancelled(true);
    }
}

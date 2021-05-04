package com.redspeaks.gang.listeners;

import com.redspeaks.gang.api.events.GangPlayerExpChangeEvent;
import com.redspeaks.gang.api.events.GangPlayerLevelUpEvent;
import com.redspeaks.gang.api.gangs.GangType;
import com.redspeaks.gang.objects.Gang;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GangsListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGainExp(GangPlayerExpChangeEvent e) {
        while(e.getPlayer().getExp() >= e.getPlayer().getGoalExp()) {
            e.getPlayer().setExp(e.getPlayer().getGoalExp() - e.getPlayer().getExp());
            e.getPlayer().levelUp();
        }
        e.getPlayer().asPlayer().playSound(e.getPlayer().asPlayer().getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2.0f, 2.0f);
        e.getPlayer().asPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7You gained &c" + (e.getTo() - e.getFrom()) + " &7exp"))
        ));
    }

    @EventHandler
    public void onLevelUp(GangPlayerLevelUpEvent e) {
        GangType gangType = e.getPlayer().getGang();
        double multiplier = gangType.getConfigOptionDouble("reward-multiplier");
        if((e.getTo() % multiplier) == 0) {
            Gang.rewardPlayer(e.getPlayer());
            return;
        }
        Player player = e.getPlayer().asPlayer();
        Location x = player.getLocation().clone().add(0, 2.0D, 0);
        Location y = player.getLocation().clone();
        for (double i = 0.0D; i <= 360.0D; i += 0.1D) {
            double R = (1.0D + 0.9D * Math.cos(10.0D * i)) * (1.0D + 0.1D * Math.cos(24.0D * i)) * (0.9D + 0.1D * Math.cos(200.0D * i)) * (1.0D + Math.sin(i));
            x.setY(y.getY() + 2.0D * R * Math.sin(i));
            x.setX(y.getX() + 2.0D * R * Math.cos(i));
            player.spawnParticle(Particle.DRIP_LAVA, (float)x.getX(), (float)x.getY(), (float)x.getZ(), 1, 0, 0, 0, 0, null);
        }
    }
}

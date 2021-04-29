package com.redspeaks.gang.api.events;

import com.redspeaks.gang.api.gangs.GangBase;
import com.redspeaks.gang.api.gangs.GangPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GangPlayerLevelUpEvent extends Event implements Cancellable {

    private final GangPlayer player;
    private final int from;
    private final int to;
    private boolean isCancelled = false;
    private GangBase gangBase;
    public GangPlayerLevelUpEvent(GangPlayer player, int from, int to, GangBase gangBase) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.gangBase = gangBase;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GangPlayer getPlayer() {
        return player;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    public GangBase getGangBase() {
        return gangBase;
    }
}

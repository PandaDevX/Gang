package com.redspeaks.gang.api.events;

import com.redspeaks.gang.api.gangs.GangPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GangPlayerExpChangeEvent extends Event {

    private final GangPlayer player;
    private final double from;
    private final double to;
    public GangPlayerExpChangeEvent(GangPlayer player, double from, double to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GangPlayer gangPlayer() {
        return player;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }
}

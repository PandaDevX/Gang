package com.redspeaks.gang.api.events;

import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.api.gangs.GangType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GangPlayerJoinEvent extends Event {

    private final GangPlayer player;
    private final GangType from;
    private final GangType to;
    public GangPlayerJoinEvent(GangPlayer player, GangType from, GangType to) {
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

    public GangPlayer getPlayer() {
        return player;
    }

    public GangType getFrom() {
        return from;
    }

    public GangType getTo() {
        return to;
    }
}

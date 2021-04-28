package com.redspeaks.gang.api.events;

import com.redspeaks.gang.api.gangs.GangPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GangPlayerLevelUpEvent extends Event {

    private final GangPlayer player;
    private final int from;
    private final int to;
    public GangPlayerLevelUpEvent(GangPlayer player, int from, int to) {
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

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}

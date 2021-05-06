package com.redspeaks.gang.api.gangs;

import org.bukkit.entity.Player;

public interface LeaderBoard {

    void showTo(GangPlayer gangPlayer);
    void showTo(Player player);
}

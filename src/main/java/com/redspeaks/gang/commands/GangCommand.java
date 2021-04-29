package com.redspeaks.gang.commands;

import com.redspeaks.gang.api.command.AbstractCommand;
import com.redspeaks.gang.api.command.CommandInfo;
import com.redspeaks.gang.api.gangs.GangPlayer;
import com.redspeaks.gang.objects.Gang;
import org.bukkit.entity.Player;

@CommandInfo(name = "gang", permission = "gang", requiresPlayer = true)
public class GangCommand extends AbstractCommand {

    @Override
    public void execute(Player player, String[] args) {
        GangPlayer gangPlayer = Gang.getPlayer(player);

    }
}

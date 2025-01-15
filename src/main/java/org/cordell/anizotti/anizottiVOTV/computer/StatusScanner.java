package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class StatusScanner extends Computer {
    public StatusScanner(Block block) {
        this.baseBlock = block;
        this.model = "status";
    }

    @Override
    public void computerClick(Player player) {
        var connectLevel = 0;
        StringBuilder message = new StringBuilder();
        for (var server : Server.servers) {
            message.append(server.name).append(" | Status: ").append(server.isWork ? "ONLINE" : "OFFLINE").append('\n');
            connectLevel += server.isWork ? 1 : 0;
        }

        message.append("Connect level: ").append(connectLevel).append('\n');
        player.sendMessage(message.toString());
    }
}

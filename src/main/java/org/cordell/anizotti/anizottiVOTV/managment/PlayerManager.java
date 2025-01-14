package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;


public class PlayerManager implements Listener {
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        try {
            MoneyManager.addMoney(200, event.getPlayer());
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

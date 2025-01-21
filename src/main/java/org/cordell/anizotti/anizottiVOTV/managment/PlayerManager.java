package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;


public class PlayerManager implements Listener {
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        try {
            MoneyManager.addMoney(200, event.getPlayer());
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            TeamManager.addPlayer2Players(event.getPlayer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ItemManager.takeAllItems(event.getPlayer());
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof Player) {
            if (TeamManager.isKittie(player)) event.setDamage(0.1);
        }
    }
}

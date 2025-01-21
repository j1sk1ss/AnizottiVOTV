package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;


public class PlayerManager implements Listener {
    @EventHandler
    private void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof Player) {
            if (TeamManager.isKittie(player)) event.setDamage(0.1);
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (TeamManager.isKittie(player)) {
                if (SpawnManager.kittiesSpawn == null) return;
                player.spigot().respawn();
                player.teleport(SpawnManager.kittiesSpawn.getLocation().add(0, 1, 0));
            }
            else if (TeamManager.isPlayer(player)) {
                if (SpawnManager.playerSpawn == null) return;
                player.spigot().respawn();
                player.teleport(SpawnManager.playerSpawn.getLocation().add(0, 1, 0));
            }
        }
    }
}

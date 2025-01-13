package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.j1sk1ss.itemmanager.manager.Manager;


public class FlashLightManager {
    public static void startLightingTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (var player : Bukkit.getOnlinePlayers()) {
                    var item = player.getInventory().getItemInMainHand();
                    if (item.getType() == Material.STICK) {
                        if (!Manager.getName(item).equals("flashLight")) return;
                        var location = player.getLocation().add(0, 1, 0);
                        if (player.getWorld().getBlockAt(location).getType() == Material.AIR) {
                            player.getWorld().getBlockAt(location).setType(Material.LIGHT);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.getWorld().getBlockAt(location).setType(Material.AIR);
                                }
                            }.runTaskLater(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 20L);
                        }
                    }
                }
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, 20L);
    }
}

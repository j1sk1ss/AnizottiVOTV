package org.cordell.anizotti.anizottiVOTV.kitties;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.computer.ComputerManager;
import org.cordell.anizotti.anizottiVOTV.computer.Generator;
import org.cordell.anizotti.anizottiVOTV.computer.Server;
import org.cordell.anizotti.anizottiVOTV.managment.DaysManager;
import org.cordell.anizotti.anizottiVOTV.managment.TeamManager;
import org.j1sk1ss.itemmanager.manager.Manager;

import java.util.Objects;
import java.util.Random;


public class KittiesManager implements Listener {
    private static int availableActions = 0;

    public static int getEnergy() {
        return availableActions;
    }

    public static boolean useEnergy(int count) {
        if (availableActions < count) return false;
        TeamManager.informKitties("Used " + count + " points of energy");
        availableActions -= count;
        return true;
    }

    public static void earnEnergy(int count) {
        availableActions += count;
        TeamManager.informKitties("Earned energy");
    }

    private static final Random random = new Random();

    public static void startRandomEvents() {
        new BukkitRunnable() {
            @Override
            public void run() {
                earnEnergy(1 + DaysManager.day);
                for (var player : Bukkit.getOnlinePlayers()) {
                    if (!isNight(player.getWorld())) continue;
                    if (TeamManager.isKittie(player)) continue;
                    int eventChance = random.nextInt(250);

                    if (DaysManager.day >= 1) {
                        if (eventChance < 66) spawnArmorStandBehindPlayer(player);
                        else if (eventChance < 133) hitPlayerWithCatSound(player);
                        else playWalkingSoundsAroundPlayer(player);
                    }

                    if (DaysManager.day >= 3) {
                        if (eventChance < 100) {
                            Generator.main.isWork = false;
                            ComputerManager.turnOffComputers();
                        }
                        else if (eventChance < 150) {
                            var server = Server.servers.get(random.nextInt(Server.servers.size()));
                            if (server.isWork) {
                                server.isWork = false;
                                ComputerManager.disconnectComputers(1);
                            }
                        }
                    }

                    if (DaysManager.day >= 6) {
                        if (eventChance < 249) {
                            for (int i = 0; i < 4; i++) {
                                var server = Server.servers.get(random.nextInt(Server.servers.size()));
                                if (server.isWork) {
                                    server.isWork = false;
                                    ComputerManager.disconnectComputers(1);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0L, 20L * (240 / DaysManager.day));
    }

    private static boolean isNight(World world) {
        long time = world.getTime();
        return time >= 13000 && time <= 23000;
    }

    private static void spawnArmorStandBehindPlayer(Player player) {
        var location = player.getLocation().clone();
        location.setYaw(location.getYaw() + 180);
        location.add(location.getDirection().multiply(-1));
        Material[] validMaterials = { Material.ANDESITE, Material.SMOOTH_STONE, Material.DEEPSLATE_TILE_SLAB };

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                for (int y = -10; y <= 10; y++) {
                    var blockLocation = player.getWorld().getHighestBlockAt(location.clone()).getLocation().add(x, y, z);
                    var belowLocation = blockLocation.clone().add(0, 1, 0);

                    if (blockLocation.getBlock().getType() == Material.AIR && belowLocation.getBlock().getType() == Material.AIR) {
                        for (var validMaterial : validMaterials) {
                            if (blockLocation.clone().add(0, -1, 0).getBlock().getType() == validMaterial) {
                                System.out.println("Spawned stand");
                                var armorStand = Objects.requireNonNull(blockLocation.getWorld()).spawn(blockLocation, ArmorStand.class);
                                armorStand.setVisible(true);
                                armorStand.setBasePlate(false);
                                armorStand.setGravity(false);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void hitPlayerWithCatSound(Player player) {
        player.damage(1.0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.0f);
    }

    private static void playWalkingSoundsAroundPlayer(Player player) {
        Location location = player.getLocation();
        for (int i = 0; i < 5; i++) {
            double offsetX = (random.nextDouble() * 2 - 1) * 3;
            double offsetZ = (random.nextDouble() * 2 - 1) * 3;

            Location soundLocation = location.clone().add(offsetX, 0, offsetZ);
            player.getWorld().playSound(soundLocation, Sound.BLOCK_GRASS_STEP, 0.5f, 1.0f);
        }
    }

    @EventHandler
    private void interactKittiesMenu(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (TeamManager.isPlayer(player)) return;
        var item = player.getInventory().getItemInMainHand();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (item.getItemMeta() == null) return;
        if (Manager.getName(item).equals("KIT-MENU")) {
            KittiesUI.openKittiesMenu(player);
            event.setCancelled(true);
        }
    }
}

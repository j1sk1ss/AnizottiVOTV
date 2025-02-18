package org.cordell.anizotti.anizottiVOTV.kitties;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
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
    private static BossBar energy;
    private static int availableActions = 0;

    public static int getEnergy() {
        return availableActions;
    }

    public static boolean useEnergy(int count) {
        if (availableActions < count) return false;
        availableActions -= count;
        if (energy != null) energy.setProgress(availableActions / 100d);
        return true;
    }

    public static void earnEnergy(int count) {
        if (availableActions + count > 100) return;
        if (energy == null) {
            energy = Bukkit.createBossBar("Energy", BarColor.GREEN, BarStyle.SOLID);
            energy.setProgress(0.0);
        }

        energy.removeAll();
        for (var player : Bukkit.getOnlinePlayers()) {
            if (TeamManager.isKittie(player)) energy.addPlayer(player);
        }

        availableActions += count;
        energy.setProgress(Math.max(availableActions / 100d, 1));
    }

    private static final Random random = new Random();

    public static void stopKitties() {
        if (energy == null) return;
        energy.removeAll();
    }

    public static BossBar getEnergyBar() {
        return energy;
    }

    public static void startEnergyGrow() {
        new BukkitRunnable() {
            @Override
            public void run() {
                earnEnergy(1 + DaysManager.day);
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0L, 20L * 10);
    }

    public static void startRandomEvents() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (var player : Bukkit.getOnlinePlayers()) {
                    if (!isNight(player.getWorld())) continue;
                    if (TeamManager.isKittie(player)) continue;
                    int eventChance = random.nextInt(250);

                    if (DaysManager.day >= 1) {
                        if (eventChance < 66) spawnArmorStandBehindPlayer(player);
                        else if (eventChance < 163) hitPlayerWithCatSound(player);
                        else playWalkingSoundsAroundPlayer(player);
                    }

                    if (DaysManager.day >= 6) {
                        if (eventChance < 50) {
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

                    if (DaysManager.day >= 10) {
                        if (eventChance < 100) {
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
            var offsetX = (random.nextDouble() * 2 - 1) * 3;
            var offsetZ = (random.nextDouble() * 2 - 1) * 3;
            Location soundLocation = location.clone().add(offsetX, 0, offsetZ);
            player.getWorld().playSound(soundLocation, Sound.BLOCK_GRASS_STEP, 0.5f, 1.0f);
        }
    }

    @EventHandler
    private void interactKittiesMenu(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var item = player.getInventory().getItemInMainHand();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (item.getItemMeta() == null) return;
        if (!TeamManager.isKittie(player)) return;
        if (Manager.getName(item).equals("KIT-MENU")) {
            KittiesUI.openKittiesMenu(player);
            event.setCancelled(true);
        }
    }
}

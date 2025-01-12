package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.j1sk1ss.itemmanager.manager.Item;
import org.j1sk1ss.itemmanager.manager.Manager;

import java.util.Objects;
import java.util.Random;


public class CargoManager implements Listener {
    private static Block targetBlock;

    public static int sendCargo(String storage) {
        int deliveryTime = new Random().nextInt(25) + 25;
        new BukkitRunnable() {
            int progress = 0;
            @Override
            public void run() {
                if (progress < deliveryTime) {
                    progress++;
                    if (progress == deliveryTime) {
                        spawnFallingBlock(targetBlock.getLocation(), Material.BEEHIVE, storage);
                        cancel();
                    }
                }
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, 20L);
        return deliveryTime;
    }

    @EventHandler
    private void interactWithCargo(PlayerInteractEvent event) {
        var block = event.getClickedBlock();
        if (block == null) return;
        var cargoName = getDataFromBlock(block, "storage");
        if (cargoName == null) return;

        var player = event.getPlayer();
        switch (cargoName) {
            case "flash-light" -> {
                var flashLight = new Item("flashLight", "", Material.STICK);
                Manager.setInteger2Container(flashLight, 1, "is_flash_light");
                Manager.giveItems(flashLight, player);
            }
            case "bread" -> {
                Manager.giveItems(new ItemStack(Material.BREAD, 16), player);
            }
            case "soup" -> {
                Manager.giveItems(new ItemStack(Material.BEETROOT_SOUP), player);
            }
            case "meat" -> {
                Manager.giveItems(new ItemStack(Material.COOKED_BEEF, 12), player);
            }
            case "apple" -> {
                Manager.giveItems(new ItemStack(Material.APPLE, 12), player);
            }
            case "horse" -> {
                var location = player.getLocation();
                var world = player.getWorld();
                var horse = (Horse) world.spawnEntity(location, EntityType.HORSE);

                horse.setCustomName("Cordell Radio-Astronomy Transport");
                horse.setCustomNameVisible(true);
                horse.setTamed(true);
                horse.setOwner(player);
                horse.setColor(Horse.Color.CHESTNUT);
                horse.setStyle(Horse.Style.WHITE_DOTS);

                var saddle = new ItemStack(Material.SADDLE);
                horse.getInventory().setSaddle(saddle);
            }
            case "sword" -> {
                Manager.giveItems(new ItemStack(Material.IRON_SWORD), player);
            }
        }

        block.setType(Material.AIR);
    }

    private static void spawnFallingBlock(Location location, Material material, String item) {
        var fallingBlock = Objects.requireNonNull(location.getWorld()).spawnFallingBlock(location.add(0, 40, 0), material.createBlockData());

        fallingBlock.setDropItem(false);
        fallingBlock.setHurtEntities(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (fallingBlock.isDead() || fallingBlock.isOnGround()) {
                    var landedLocation = fallingBlock.getLocation();
                    var landedBlock = landedLocation.getBlock();

                    landedBlock.setType(material);
                    addDataToBlock(landedBlock, "storage", item);
                    cancel();
                }
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, 1);
    }

    private static void addDataToBlock(Block block, String keyName, String value) {
        var state = block.getState();
        if (state instanceof TileState tileState) {
            var key = new NamespacedKey(AnizottiVOTV.getPlugin(AnizottiVOTV.class), keyName);
            var pdc = tileState.getPersistentDataContainer();
            pdc.set(key, PersistentDataType.STRING, value);
            tileState.update();
        }
    }

    private static String getDataFromBlock(Block block, String keyName) {
        var state = block.getState();
        if (state instanceof TileState tileState) {
            var key = new NamespacedKey(AnizottiVOTV.getPlugin(AnizottiVOTV.class), keyName);
            var pdc = tileState.getPersistentDataContainer();
            return pdc.get(key, PersistentDataType.STRING);
        }

        return null;
    }

    // Non-static logic

    public CargoManager(Block block) {
        targetBlock = block;
    }
}

package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.Utils;
import org.j1sk1ss.itemmanager.manager.Item;
import org.j1sk1ss.itemmanager.manager.Manager;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;


public class CargoManager implements Listener {
    public static Block targetBlock;

    public static int sendCargo(String storage) {
        int deliveryTime = new Random().nextInt(25) + 1;
        new BukkitRunnable() {
            int progress = 0;
            @Override
            public void run() {
                if (progress <= deliveryTime) {
                    if (++progress == deliveryTime) {
                        spawnFallingBlock(targetBlock.getLocation(), storage);
                        cancel();
                    }
                }
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, 20L);
        return deliveryTime;
    }

    public static int receiveCargo(Player sender) {
        var cargoBlock = targetBlock.getRelative(0, 1, 0);
        if (cargoBlock.getType() != Material.BEEHIVE) return -1;

        var isStorage = getDataFromBlock(cargoBlock, "storage");
        if (isStorage != null) return -1;

        int deliveryTime = new Random().nextInt(25) + 1;
        new BukkitRunnable() {
            int progress = 0;
            @Override
            public void run() {
                if (progress < deliveryTime) {
                    progress++;
                    if (progress == deliveryTime) {
                        try {
                            MoneyManager.addMoney(parseCargo(cargoBlock), sender);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        cargoBlock.setType(Material.AIR);
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
        var player = event.getPlayer();
        if (block == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        var isCargo = getDataFromBlock(block, "is_cargo");
        if (isCargo != null) {
            if (isCargo.equals("true")) {
                var isAdd = false;
                var item = player.getInventory().getItemInMainHand();
                for (int i = 0; i < 6; i++) {
                    var result = getDataFromBlock(block, "storage_" + i);
                    if (result != null) continue;
                    addDataToBlock(block, "storage_" + i, Utils.serializeItemStack(item));
                    player.getInventory().setItemInMainHand(null);
                    isAdd = true;
                    break;
                }

                if (!isAdd) {
                    player.sendMessage("Cargo is full");
                }

                event.setCancelled(true);
                return;
            }
        }

        if (targetBlock != null) {
            if (player.getInventory().getItemInMainHand().getType() == Material.BEEHIVE && block.getLocation().equals(targetBlock.getLocation())) {
                player.getInventory().setItemInMainHand(null);
                var blockAbove = block.getRelative(0, 1, 0);
                blockAbove.setType(Material.BEEHIVE);
                addDataToBlock(blockAbove, "is_cargo", "true");
                return;
            }
        }

        var cargoName = getDataFromBlock(block, "storage");
        if (cargoName == null) return;

        switch (cargoName) {
            case "flash-light" -> {
                var flashLight = new Item("flashLight", "", Material.STICK);
                Manager.setInteger2Container(flashLight, 1, "is_flash_light");
                Manager.giveItems(flashLight, player);
            }
            case "bread" -> Manager.giveItems(new ItemStack(Material.BREAD, 16), player);
            case "soup" -> Manager.giveItems(new ItemStack(Material.BEETROOT_SOUP), player);
            case "meat" -> Manager.giveItems(new ItemStack(Material.COOKED_BEEF, 12), player);
            case "apple" -> Manager.giveItems(new ItemStack(Material.APPLE, 12), player);
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
            case "sword" -> Manager.giveItems(new ItemStack(Material.IRON_SWORD), player);
            case "cargo" -> Manager.giveItems(new ItemStack(Material.BEEHIVE), player);
            case "shovel" -> player.performCommand("give @p minecraft:wooden_shovel[can_break={predicates:[{blocks:\"suspicious_sand\"},{blocks:\"suspicious_gravel\"}]}] 1");
            case "key" -> player.performCommand("give @p minecraft:oak_button[minecraft:can_place_on={predicates:[{blocks:granite}]}] 1");
            case "secret-key" -> player.performCommand("give @p minecraft:oak_button[minecraft:can_place_on={predicates:[{blocks:polished_granite}]}] 1");
            case "music-box" -> player.performCommand("give @p minecraft:jukebox[minecraft:can_place_on={predicates:[{blocks:smooth_stone}]}] 1");
        }

        block.setType(Material.AIR);
    }

    private static final int signalPrice = 50;

    private static int parseCargo(Block block) {
        int price = 0;
        for (int i = 0; i < 6; i++) {
            var data = getDataFromBlock(block, "storage_" + i);
            if (data != null) {
                var item = Utils.deserializeItemStack(data);
                if (item != null) {
                    var isSignal = Manager.getIntegerFromContainer(item, "is_signal");
                    if (isSignal != -1) {
                        var tempPrice = signalPrice;
                        var signalType = Manager.getIntegerFromContainer(item, "signal_type");
                        var decrypted = Manager.getIntegerFromContainer(item, "is_decrypted");
                        if (decrypted != -1) {
                            tempPrice *= 2;
                        }

                        tempPrice *= signalType;
                        price += tempPrice;
                    }
                    else {
                        price += item.getAmount();
                    }
                }
            }
        }

        return price;
    }

    private static void spawnFallingBlock(Location location, String item) {
        var fallingBlock = Objects.requireNonNull(location.getWorld()).spawnFallingBlock(location.add(0, 40, 0), Material.BEEHIVE.createBlockData());

        fallingBlock.setDropItem(false);
        fallingBlock.setHurtEntities(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (fallingBlock.isDead() || fallingBlock.isOnGround()) {
                    var landedLocation = fallingBlock.getLocation();
                    var landedBlock = landedLocation.getBlock();

                    landedBlock.setType(Material.BEEHIVE);
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

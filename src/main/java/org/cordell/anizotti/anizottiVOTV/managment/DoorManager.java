package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.cordell.anizotti.anizottiVOTV.common.BlockManager;
import org.j1sk1ss.itemmanager.manager.Item;
import org.j1sk1ss.itemmanager.manager.Manager;


public class DoorManager implements Listener {
    @EventHandler
    private void onDoorInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        var block = event.getClickedBlock();
        var key = player.getInventory().getItemInMainHand();
        if (block == null) return;
        if (!block.getType().equals(Material.FURNACE)) return;
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 1.0f, 1.0f);
        if (Manager.getIntegerFromContainer(key, "is-key") == -1 &&
                Manager.getIntegerFromContainer(key, "is-upgrade") == -1) return;

        if (Manager.getIntegerFromContainer(key, "is-upgrade") == 1) {
            BlockManager.addDataToBlock(block, "door-level", 2 + "");
            player.getInventory().setItemInMainHand(null);
            event.setCancelled(true);
            return;
        }

        var keyLevel = Manager.getIntegerFromContainer(key, "key-level");
        var doorLevel = BlockManager.getDataFromBlock(block, "door-level");
        if (doorLevel == null) return;
        if (keyLevel == -1) return;

        if (keyLevel == Integer.parseInt(doorLevel)) {
            Block[] nearbyBlocks = {
                block.getRelative(BlockFace.NORTH),
                block.getRelative(BlockFace.SOUTH),
                block.getRelative(BlockFace.EAST),
                block.getRelative(BlockFace.WEST)
            };

            for (var nearbyBlock : nearbyBlocks) {
                if (nearbyBlock.getType() == Material.IRON_DOOR) toggleDoor(player, nearbyBlock);
            }
        }

        event.setCancelled(true);
    }

    private static void toggleDoor(Player player, Block doorBlock) {
        var blockData = doorBlock.getBlockData();
        if (blockData instanceof org.bukkit.block.data.type.Door door) {
            door.setOpen(!door.isOpen());
            if (door.isOpen()) player.getWorld().playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.0f);
            else player.getWorld().playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.0f);
            doorBlock.setBlockData(door);
        }
    }

    public static void giveKey(Player player, int level) {
        var key = new Item("Key", "Level: " + level, Material.TRIAL_KEY);
        Manager.setInteger2Container(key, 1, "is-key");
        Manager.setInteger2Container(key, level, "key-level");
        Manager.giveItems(key, player);
    }

    public static void setDoor(Block door, int level) {
        BlockManager.addDataToBlock(door, "door-level", level + "");
    }
}

package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;


public class ComputerManager implements Listener {
    public static ArrayList<Computer> computers = new ArrayList<>();

    @EventHandler
    private void onComputer(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var block = event.getClickedBlock();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        for (var computer : computers) {
            if (computer.baseBlock.equals(block)) {
                computer.computerClick(player);
                break;
            }
        }
    }

    public static void turnOffComputers() {
        for (var computer : computers) {
            computer.isPowered = false;
        }
    }

    public static void turnOnComputers() {
        for (var computer : computers) {
            computer.isPowered = true;
        }
    }

    public static void disconnectComputers(int count) {
        Computer.connectLevel = Math.max(0, Computer.connectLevel - count);
    }

    public static void connectComputers(int count) {
        Computer.connectLevel = Math.min(Server.servers.size(), Computer.connectLevel + count);
    }
}

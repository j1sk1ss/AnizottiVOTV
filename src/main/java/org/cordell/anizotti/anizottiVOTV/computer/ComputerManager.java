package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;


public class ComputerManager implements Listener {
    public static ArrayList<Computer> computers = new ArrayList<>();

    @EventHandler
    private void onComputer(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var block = event.getClickedBlock();
        for (var computer : computers) {
            if (computer.baseBlock.equals(block)) {
                computer.computerClick(player);
                break;
            }
        }
    }

    public static void breakComputers() {
        for (var computer : computers) {
            computer.isPowered = false;
        }
    }

    public static void fixComputers() {
        for (var computer : computers) {
            computer.isPowered = true;
        }
    }
}

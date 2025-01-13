package org.cordell.anizotti.anizottiVOTV.admin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.inventory.EquipmentSlot;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.computer.*;
import org.cordell.anizotti.anizottiVOTV.computer.signals.Converter;
import org.cordell.anizotti.anizottiVOTV.computer.signals.Finder;
import org.cordell.anizotti.anizottiVOTV.managment.CargoManager;

import org.j1sk1ss.itemmanager.manager.Manager;


public class AdminManager implements Listener {
    @EventHandler
    private void spawnObject(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var block = event.getClickedBlock();
        var item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return;
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        var option = Manager.getName(item);
        switch (option) {
            case "server-spawn":
                var server = new Server(block, String.join("", Manager.getLoreLines(item)));
                ComputerManager.computers.add(server);
                Server.servers.add(server);
                System.out.println("Server spawned!");
                break;
            case "generator-spawn":
                Generator.main = new Generator(block, "generator");
                ComputerManager.computers.add(Generator.main);
                System.out.println("Generator spawned!");
                break;
            case "shop-spawn":
                ComputerManager.computers.add(new Shop(block));
                System.out.println("Shop spawned!");
                break;
            case "finder-spawn":
                ComputerManager.computers.add(new Finder(block));
                System.out.println("Finder spawned!");
                break;
            case "converter-spawn":
                ComputerManager.computers.add(new Converter(block));
                System.out.println("Converter spawned!");
                break;
            case "cargo-spawn":
                Bukkit.getPluginManager().registerEvents(new CargoManager(block), AnizottiVOTV.getPlugin(AnizottiVOTV.class));
                System.out.println("Cargo spawned!");
                break;
            case "status-spawn":
                ComputerManager.computers.add(new StatusScanner(block));
                System.out.println("Status spawned!");
                break;
            default:
                return;
        }

        event.setCancelled(true);
    }
}

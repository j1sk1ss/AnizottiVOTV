package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import org.cordell.anizotti.anizottiVOTV.computer.ComputerManager;
import org.cordell.anizotti.anizottiVOTV.computer.Generator;
import org.cordell.anizotti.anizottiVOTV.computer.Server;
import org.cordell.anizotti.anizottiVOTV.kitties.KittiesManager;

import org.j1sk1ss.itemmanager.manager.Item;
import org.j1sk1ss.itemmanager.manager.Manager;

import java.io.IOException;
import java.util.Objects;


public class MainManager {
    public static void startEvent() {
        Server.serverCrush();
        Generator.generatorCrush();
        DaysManager.startDayTimer();
        KittiesManager.startRandomEvents();
        KittiesManager.startEnergyGrow();
        FlashLightManager.startLightingTask();

        ComputerManager.turnOnComputers();
        ComputerManager.connectComputers(Server.servers.size());

        for (var p : Bukkit.getOnlinePlayers()) {
            try {
                MoneyManager.addMoney(200, p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            p.setGameMode(GameMode.ADVENTURE);
            ItemManager.takeAllItems(p);

            if (TeamManager.isPlayer(p)) {
                if (SpawnManager.playerSpawn == null) continue;
                p.teleport(SpawnManager.playerSpawn.getLocation().add(0, 1, 0));
            }
            else if (TeamManager.isKittie(p)) {
                if (SpawnManager.kittiesSpawn == null) continue;
                p.teleport(SpawnManager.kittiesSpawn.getLocation().add(0, 1, 0));
                Manager.giveItems(new Item("KIT-MENU", "KIT-MENU", Material.DIAMOND), p);
                Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_SCALE)).setBaseValue(0.5);
            }
        }
    }
}

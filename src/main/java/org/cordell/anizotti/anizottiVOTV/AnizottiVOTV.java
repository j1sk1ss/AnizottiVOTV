package org.cordell.anizotti.anizottiVOTV;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cordell.anizotti.anizottiVOTV.admin.AdminManager;
import org.cordell.anizotti.anizottiVOTV.admin.CommandManager;
import org.cordell.anizotti.anizottiVOTV.common.LocationManager;
import org.cordell.anizotti.anizottiVOTV.computer.*;
import org.cordell.anizotti.anizottiVOTV.computer.signals.Converter;
import org.cordell.anizotti.anizottiVOTV.computer.signals.Finder;
import org.cordell.anizotti.anizottiVOTV.kitties.KittiesManager;
import org.cordell.anizotti.anizottiVOTV.managment.*;
import org.cordell.com.cordelldb.manager.Manager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public final class AnizottiVOTV extends JavaPlugin {
    @Override
    public void onEnable() {
        var locationManager = new Manager("anizottiVOTV_computers.txt");
        for (var key : locationManager.getKeySet()) {
            var data = key.split("_");
            try {
                var location = LocationManager.stringToLocation(locationManager.getString(key));
                if (location == null) continue;
                switch (data[1]) {
                    case "finder" -> ComputerManager.computers.add(new Finder(Bukkit.getWorlds().getFirst().getBlockAt(location)));
                    case "converter" -> ComputerManager.computers.add(new Converter(Bukkit.getWorlds().getFirst().getBlockAt(location)));
                    case "server" -> {
                        var server = new Server(Bukkit.getWorlds().getFirst().getBlockAt(location), data[0]);
                        ComputerManager.computers.add(server);
                        Server.servers.add(server);
                    }
                    case "shop" -> ComputerManager.computers.add(new Shop(Bukkit.getWorlds().getFirst().getBlockAt(location)));
                    case "status" -> ComputerManager.computers.add(new StatusScanner(Bukkit.getWorlds().getFirst().getBlockAt(location)));
                    case "generator" -> {
                        var generator = new Generator(Bukkit.getWorlds().getFirst().getBlockAt(location), data[0]);
                        ComputerManager.computers.add(generator);
                        Generator.main = generator;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        locationManager.stop();

        var upgradesManager = new Manager("anizottiVOTV_other.txt");
        Server.durability = upgradesManager.getInt("server-upgrade");
        Finder.speed = upgradesManager.getInt("scanner-upgrade");
        Converter.speed = upgradesManager.getInt("converter-upgrade");
        DaysManager.day = upgradesManager.getInt("days");
        try {
            var cargoLocation = LocationManager.stringToLocation(upgradesManager.getString("cargo"));
            if (cargoLocation != null) {
                Bukkit.getPluginManager().registerEvents(new CargoManager(Bukkit.getWorlds().getFirst().getBlockAt(cargoLocation)), AnizottiVOTV.getPlugin(AnizottiVOTV.class));
            }

            var pSpawnLocation = LocationManager.stringToLocation(upgradesManager.getString("p-spawn"));
            var kSpawnLocation = LocationManager.stringToLocation(upgradesManager.getString("k-spawn"));
            if (pSpawnLocation != null) SpawnManager.playerSpawn = Bukkit.getWorlds().getFirst().getBlockAt(pSpawnLocation);
            if (kSpawnLocation != null) SpawnManager.kittiesSpawn = Bukkit.getWorlds().getFirst().getBlockAt(kSpawnLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        upgradesManager.stop();

        Server.durability = Math.max(4, Server.durability);
        Finder.speed = Math.max(1, Finder.speed);
        Converter.speed = Math.max(1, Converter.speed);
        DaysManager.day = Math.max(1, DaysManager.day);

        for (var listener : List.of(
            new AdminManager(), new ComputerManager(), new EatManager(), new KittiesManager(),
            new MobManager(), new JunkManager(), new DoorManager(), new PlayerManager()
        ))
            Bukkit.getPluginManager().registerEvents(listener, this);

        var command_manager = new CommandManager();
        for (var command : Arrays.asList(
            "give-money", "server-spawn", "generator-spawn", "shop-spawn",
            "finder-spawn", "converter-spawn", "cargo-spawn", "money", "status-spawn",
            "lock-door", "to-players", "to-kitties", "fix-all", "player-spawn", "start-event"
        ))
            Objects.requireNonNull(getCommand(command)).setExecutor(command_manager);
    }

    @Override
    public void onDisable() {
        QuotaManager.stopQuota();
        KittiesManager.stopKitties();
        var locationManager = new Manager("anizottiVOTV_computers.txt");
        for (var computer : ComputerManager.computers) {
            try {
                locationManager.setString(computer.name + "_" + computer.model, LocationManager.locationToString(computer.baseBlock.getLocation()));
                locationManager.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        locationManager.stop();

        var upgradesManager = new Manager("anizottiVOTV_other.txt");
        try {
            upgradesManager.setInt("server-upgrade", Server.durability);
            upgradesManager.setInt("scanner-upgrade", Finder.speed);
            upgradesManager.setInt("converter-upgrade", Converter.speed);
            if (CargoManager.targetBlock != null) upgradesManager.setString("cargo", LocationManager.locationToString(CargoManager.targetBlock.getLocation()));
            if (SpawnManager.playerSpawn != null) upgradesManager.setString("p-spawn", LocationManager.locationToString(SpawnManager.playerSpawn.getLocation()));
            if (SpawnManager.kittiesSpawn != null) upgradesManager.setString("k-spawn", LocationManager.locationToString(SpawnManager.kittiesSpawn.getLocation()));
            upgradesManager.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        upgradesManager.stop();
        for (var p : Bukkit.getOnlinePlayers()) {
            ItemManager.returnItems(p);
        }
    }
}

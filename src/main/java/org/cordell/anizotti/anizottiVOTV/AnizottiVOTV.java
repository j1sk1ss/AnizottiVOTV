package org.cordell.anizotti.anizottiVOTV;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cordell.anizotti.anizottiVOTV.admin.AdminManager;
import org.cordell.anizotti.anizottiVOTV.admin.CommandManager;
import org.cordell.anizotti.anizottiVOTV.computer.ComputerManager;
import org.cordell.anizotti.anizottiVOTV.computer.Generator;
import org.cordell.anizotti.anizottiVOTV.computer.Server;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public final class AnizottiVOTV extends JavaPlugin {
    @Override
    public void onEnable() {
        for (var listener : List.of(new AdminManager(), new ComputerManager()))
            Bukkit.getPluginManager().registerEvents(listener, this);

        var command_manager = new CommandManager();
        for (var command : Arrays.asList(
                "give-money", "server-spawn", "generator-spawn", "shop-spawn",
                "finder-spawn", "converter-spawn", "cargo-spawn", "money"
        ))
            Objects.requireNonNull(getCommand(command)).setExecutor(command_manager);

        Server.serverCrush();
        Generator.generatorCrush();
    }

    @Override
    public void onDisable() {

    }
}

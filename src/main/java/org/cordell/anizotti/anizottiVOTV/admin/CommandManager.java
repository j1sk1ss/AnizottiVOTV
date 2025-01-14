package org.cordell.anizotti.anizottiVOTV.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cordell.anizotti.anizottiVOTV.managment.MoneyManager;
import org.j1sk1ss.itemmanager.manager.Item;
import org.j1sk1ss.itemmanager.manager.Manager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        var player = (Player) commandSender;
        switch (command.getName()) {
            case "lock-door" -> Manager.giveItems(new Item("lock-door", strings[0]), player);
            case "status-spawn" -> Manager.giveItems(new Item("status-spawn", "status-spawn"), player);
            case "server-spawn" -> Manager.giveItems(new Item("server-spawn", strings[0]), player);
            case "generator-spawn" -> Manager.giveItems(new Item("generator-spawn", "generator-spawn"), player);
            case "shop-spawn" -> Manager.giveItems(new Item("shop-spawn", "shop-spawn"), player);
            case "finder-spawn" -> Manager.giveItems(new Item("finder-spawn", "finder-spawn"), player);
            case "converter-spawn" -> Manager.giveItems(new Item("converter-spawn", "converter-spawn"), player);
            case "cargo-spawn" -> Manager.giveItems(new Item("cargo-spawn", "cargo-spawn"), player);
            case "give-money" -> {
                try {
                    MoneyManager.addMoney(1000, player);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "money" -> {
                try {
                    player.sendMessage("Balance: " + MoneyManager.getMoney(player) + "$");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return true;
    }
}

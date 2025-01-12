package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.cordell.anizotti.anizottiVOTV.managment.CargoManager;
import org.cordell.anizotti.anizottiVOTV.managment.MoneyManager;

import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.LittleButton;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.io.IOException;
import java.util.List;


public class Shop extends Computer {
    private static final double flashLightPrice = 100d;
    private static final double breadPrice = 50d;
    private static final double soupPrice = 150d;
    private static final double meatPrice = 300d;
    private static final double applePrice = 65d;
    private static final double horsePrice = 1250d;
    private static final double swordPrice = 1000d;

    private static final MenuWindow shopUpgradeInterface = new MenuWindow(List.of(
        new Panel(List.of(
            new LittleButton(new Margin(0, 0, 0), "Buy flashlight", flashLightPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(flashLightPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("flash-light") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }),
            new LittleButton(new Margin(1, 0, 0), "Buy bread x16", breadPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(breadPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("bread") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }),
            new LittleButton(new Margin(2, 0, 0), "Buy soup", soupPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(soupPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("soup") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }),
            new LittleButton(new Margin(3, 0, 0), "Buy meat x12", meatPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(meatPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("meat") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }),
            new LittleButton(new Margin(4, 0, 0), "Buy apple x12", applePrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(applePrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("apple") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }),
            new LittleButton(new Margin(5, 0, 0), "Buy horse", horsePrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(horsePrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("horse") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }),
            new LittleButton(new Margin(6, 0, 0), "Buy sword", swordPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(swordPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("sword") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
        ), "shop", MenuSizes.SixLines)
    ), "shopMenu");

    // Non-static logic

    public Shop(Block block) {
        this.baseBlock = block;
        this.isPowered = true;
    }

    @Override
    public void computerClick(Player player) {
        if (!this.isPowered) return;
        shopUpgradeInterface.getPanel("shop").getView(player);
    }
}

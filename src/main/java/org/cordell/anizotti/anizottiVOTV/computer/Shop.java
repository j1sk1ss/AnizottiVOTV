package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.cordell.anizotti.anizottiVOTV.computer.signals.Converter;
import org.cordell.anizotti.anizottiVOTV.computer.signals.Finder;
import org.cordell.anizotti.anizottiVOTV.managment.CargoManager;
import org.cordell.anizotti.anizottiVOTV.managment.MoneyManager;

import org.cordell.anizotti.anizottiVOTV.managment.TeamManager;
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
    private static final double shovelPrice = 500d;
    private static final double cargoPrice = 100d;
    private static final double keyPrice = 500d;
    private static final double secretPrice = 2500d;
    private static final double musicBoxPrice = 499d;
    private static final double doorLockUpgrade = 2000d;

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
            }, Material.STICK),
            new LittleButton(new Margin(1, 0, 0), "Buy bread x16", breadPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(breadPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("bread") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.BREAD),
            new LittleButton(new Margin(2, 0, 0), "Buy soup", soupPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(soupPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("soup") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.BEETROOT_SOUP),
            new LittleButton(new Margin(3, 0, 0), "Buy meat x12", meatPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(meatPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("meat") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.COOKED_BEEF),
            new LittleButton(new Margin(4, 0, 0), "Buy apple x12", applePrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(applePrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("apple") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.APPLE),
            new LittleButton(new Margin(5, 0, 0), "Buy horse", horsePrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(horsePrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("horse") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.IRON_HORSE_ARMOR),
            new LittleButton(new Margin(6, 0, 0), "Buy sword", swordPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(swordPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("sword") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.DIAMOND_SWORD),
            new LittleButton(new Margin(7, 0, 0), "Buy container", cargoPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(cargoPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("cargo") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.BEEHIVE),
            new LittleButton(new Margin(8, 0, 0), "Sell container", "$$$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                var receiveTime = CargoManager.receiveCargo(player);
                if (receiveTime != -1) {
                    player.sendMessage("Sending time is: " + receiveTime + "s");
                }
                else {
                    player.sendMessage("No cargo");
                }
            }, Material.EMERALD_BLOCK),
            new LittleButton(new Margin(9, 0, 0), "Buy shovel", shovelPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(shovelPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("shovel") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.DIAMOND_SHOVEL),
            new LittleButton(new Margin(10, 0, 0), "Buy key 1", keyPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(keyPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("key") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.TRIAL_KEY),
            new LittleButton(new Margin(11, 0, 0), "Buy key 2", secretPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(secretPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("secret-key") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.TRIAL_KEY),
            new LittleButton(new Margin(12, 0, 0), "Buy music box", musicBoxPrice + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(musicBoxPrice, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("music-box") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.JUKEBOX),
            new LittleButton(new Margin(13, 0, 0), "Buy door-lock upgrade", doorLockUpgrade + "$", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(doorLockUpgrade, player)) {
                        player.sendMessage("Delivery time is: " + CargoManager.sendCargo("lock-upgrade") + "s");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.OMINOUS_TRIAL_KEY),
            new LittleButton(new Margin(17, 0, 0), "Upgrades", "", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                menu.getPanel("upgrade").getView(player);
            }, Material.ARROW)
        ), "shop", MenuSizes.TwoLines),
        new Panel(List.of(
            new LittleButton(
                    new Margin(0, 0, 0), "Upgrade scan speed",
                    "Cost: " + Finder.speed * 1000 + "\nLevel: " + Finder.speed, (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (Finder.speed >= 8) {
                        player.sendMessage("Scanner max upgrade speed");
                        return;
                    }

                    if (MoneyManager.removeMoney(Finder.speed * 1000, player)) {
                        Finder.speed += 1;
                        menu.getPanel("upgrade").getComponent("Upgrade scan speed", LittleButton.class)
                                .setLore("Cost: " + Finder.speed * 1000 + "\nLevel: " + Finder.speed);
                        menu.getPanel("upgrade").getView(player);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.ARROW),
            new LittleButton(
                    new Margin(1, 0, 0), "Upgrade decode speed",
                    "Cost: " + Converter.speed * 1250 + "\nLevel: " + Converter.speed, (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (Converter.speed >= 6) {
                        player.sendMessage("Converter max upgrade speed");
                        return;
                    }

                    if (MoneyManager.removeMoney(Converter.speed * 1250, player)) {
                        Converter.speed += 1;
                        menu.getPanel("upgrade").getComponent("Upgrade decode speed", LittleButton.class)
                                .setLore("Cost: " + Converter.speed * 1250 + "\nLevel: " + Converter.speed);
                        menu.getPanel("upgrade").getView(player);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.ARROW),
            new LittleButton(
                    new Margin(2, 0, 0), "Upgrade servers",
                    "Cost: " + Server.durability * 800 + "\nLevel: " + Server.durability, (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                try {
                    if (MoneyManager.removeMoney(Server.durability * 800, player)) {
                        Server.durability += 1;
                        menu.getPanel("upgrade").getComponent("Upgrade servers", LittleButton.class)
                                .setLore("Cost: " + Server.durability * 800 + "\nLevel: " + Server.durability);
                        menu.getPanel("upgrade").getView(player);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Material.ARROW)
        ), "upgrade", MenuSizes.OneLine)
    ), "shopMenu");

    // Non-static logic

    public Shop(Block block) {
        this.baseBlock = block;
        this.isPowered = true;
        this.model = "shop";
    }

    @Override
    public void computerClick(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1.0f, 1.0f);
        if (TeamManager.isKittie(player)) {
            player.sendMessage("Meow meow meow meow meow meow meow meow");
            return;
        }

        if (!this.isPowered) {
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1.0f, 1.0f);
            player.sendMessage("Seems shop powered off...");
            return;
        }

        shopUpgradeInterface.getPanel("shop").getView(player);
    }
}

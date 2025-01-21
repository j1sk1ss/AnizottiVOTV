package org.cordell.anizotti.anizottiVOTV.computer.signals;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.computer.Computer;
import org.cordell.anizotti.anizottiVOTV.managment.DaysManager;
import org.cordell.anizotti.anizottiVOTV.managment.QuotaManager;
import org.cordell.anizotti.anizottiVOTV.managment.TeamManager;

import org.j1sk1ss.itemmanager.manager.Item;
import org.j1sk1ss.itemmanager.manager.Manager;

import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.ClickArea;
import org.j1sk1ss.menuframework.objects.interactive.components.LittleButton;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.cordell.anizotti.anizottiVOTV.computer.signals.Signal.generateSignal;


public class Finder extends Computer {
    public static int speed = 1;

    private static int x = 0;
    private static int y = 0;
    public static boolean isBusy = false;

    private static final ArrayList<Signal> signals = new ArrayList<>();
    private static final int MAX_SIGNALS = 10;

    private static void startSignalGeneration() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (signals.size() >= MAX_SIGNALS) return;
                var curSignals = Signals.signals.get(DaysManager.day);
                if (curSignals == null) curSignals = Signals.defaultSignals;
                signals.add(generateSignal(50, 50, curSignals.get(new Random().nextInt(Signals.signals.get(DaysManager.day).size())), new Random().nextInt(DaysManager.day)));
            }
        }.runTaskTimerAsynchronously(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, 60 * 20L);
    }

    private static final MenuWindow finderInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new ClickArea(new Margin(0, 4, 8), null, "space", ""), // Space

                new LittleButton(new Margin(41, 0, 0), "Refresh", "Refresh signals", (event, menu) -> {
                    var slots = new Margin(36, 2, 4).getSlots();
                    for (var i = 0; i < Math.min(slots.size(), Finder.signals.size()); i++) {
                        var signal = Finder.signals.get(i);
                        event.getInventory().setItem(
                                slots.get(i),
                                new Item(
                                        "Signal type " + signal.getType(),
                                        "X: " + signal.getX() + " Y: " + signal.getY(),
                                        Material.NETHER_STAR
                                )
                        );
                    }
                }, Material.GOLD_INGOT),

                new LittleButton(new Margin(51, 0, 0), "Down", "Set down", (event, menu) -> {
                    event.getWhoClicked().sendMessage(x + ":" + y);
                    moveTelescope(event, 0, 1);
                }, Material.GOLD_INGOT), // Down

                new LittleButton(new Margin(52, 0, 0), "Right", "Set right", (event, menu) -> {
                    event.getWhoClicked().sendMessage(x + ":" + y);
                    moveTelescope(event, 1, 0);
                }, Material.GOLD_INGOT), // Right

                new LittleButton(new Margin(42, 0, 0), "Up", "Set up", (event, menu) -> {
                    event.getWhoClicked().sendMessage(x + ":" + y);
                    moveTelescope(event, 0, -1);
                }, Material.GOLD_INGOT), // Up

                new LittleButton(new Margin(50, 0, 0), "Left", "Set left", (event, menu) -> {
                    event.getWhoClicked().sendMessage(x + ":" + y);
                    moveTelescope(event, -1, 0);
                }, Material.GOLD_INGOT), // Left

                new LittleButton(new Margin(53, 0, 0), "Scan", "Scan signal", (event, menu) -> {
                    var player = (Player) event.getWhoClicked();
                    if (Finder.isBusy) {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                        player.sendMessage("Scanner is busy.");
                        return;
                    }

                    var signal = scanSignal(x + 4, y + 2);
                    if (signal != null) {
                        Finder.isBusy = true;
                        new BukkitRunnable() {
                            int progress = 0;
                            final int delay = (signal.getType() + 1) * 15;

                            @Override
                            public void run() {
                                player.sendMessage("Scan progress: " + Math.round(((double) progress / delay) * 100) + "%");
                                if (++progress < delay) return;

                                var signalBody = new Item("Signal", signal.getData());
                                Manager.setInteger2Container(signalBody, 1, "is_signal");
                                Manager.setInteger2Container(signalBody, signal.getType(), "signal_type");
                                Manager.giveItems(signalBody, player);

                                moveTelescope(event, 0, 0);
                                QuotaManager.completeQuota(1);
                                Finder.isBusy = false;
                                this.cancel();
                            }
                        }.runTaskTimer(
                            AnizottiVOTV.getPlugin(AnizottiVOTV.class),
                            0,
                            ((signal.getType() + 1) / Math.min(speed, 1)) * (20L * (6 / Math.min(Finder.connectLevel, 1)))
                        );
                    }
                }, Material.GOLD_INGOT) // Scan signal
            ), "finder", MenuSizes.SixLines, "\u10F2"
        )
    ), "finderMenu");

    private static void moveTelescope(InventoryClickEvent event, int xOperation, int yOperation) {
        x += xOperation;
        y += yOperation;
        drawSpace(event.getInventory(), x, y);
    }

    private static void drawSpace(Inventory inventory, int x, int y) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                inventory.setItem(j * 9 + i, new Item("", "", Material.BLACK_STAINED_GLASS_PANE));
            }
        }

        inventory.setItem(4, new Item("", "", Material.GREEN_STAINED_GLASS_PANE));
        inventory.setItem(13, new Item("", "", Material.GREEN_STAINED_GLASS_PANE));

        for (var sig : signals) {
            if (sig.getY() < y || sig.getY() >= y + 4 || sig.getX() < x || sig.getX() > x + 8) continue;
            inventory.setItem((sig.getY() - y) * 9 + sig.getX() - x, new Item("signal", "Type: " + sig.getType(), Material.RED_STAINED_GLASS_PANE));
        }
    }

    private static Signal scanSignal(int x, int y) {
        Signal signal = null;
        for (var sig : signals) {
            if (sig.getX() == x && sig.getY() == y) {
                signal = sig;
                break;
            }
        }

        signals.remove(signal);
        return signal;
    }

    // Non-static logic

    public Finder(Block baseBlock) {
        startSignalGeneration();
        this.baseBlock = baseBlock;
        this.isPowered = true;
        this.model = "finder";
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
            player.sendMessage("Seems finder powered off...");
            return;
        }

        finderInterface.getPanel("finder").getView(player);
    }
}

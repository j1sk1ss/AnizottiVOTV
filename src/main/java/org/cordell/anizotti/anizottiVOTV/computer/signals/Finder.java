package org.cordell.anizotti.anizottiVOTV.computer.signals;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.computer.Computer;

import org.j1sk1ss.itemmanager.manager.Item;
import org.j1sk1ss.itemmanager.manager.Manager;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.Bar;
import org.j1sk1ss.menuframework.objects.interactive.components.ClickArea;
import org.j1sk1ss.menuframework.objects.interactive.components.LittleButton;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Direction;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.ArrayList;
import java.util.List;

import static org.cordell.anizotti.anizottiVOTV.computer.signals.Signal.generateSignal;


public class Finder extends Computer {
    private static final ArrayList<Signal> signals = new ArrayList<>();
    private static final int MAX_SIGNALS = 10;

    private static void startSignalGeneration(int maxX, int maxY, int intervalSeconds) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (signals.size() >= MAX_SIGNALS) return;
                signals.add(generateSignal(maxX, maxY, ""));
            }
        }.runTaskTimerAsynchronously(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, intervalSeconds * 20L);
    }

    private static final MenuWindow finderInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new ClickArea(new Margin(0, 4, 8), null, "space", "0;0"), // Signals
                new Bar(new Margin(45, 1, 5), Direction.Right),
                new LittleButton(new Margin(42, 1, 1), "Up", "Set up", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    moveTelescope(event, space, 0, -1);
                }), // Up
                new LittleButton(new Margin(50, 1, 1), "Left", "Set left", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    moveTelescope(event, space, -1, 0);
                }), // Left
                new LittleButton(new Margin(51, 1, 1), "Down", "Set down", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    moveTelescope(event, space, 0, 1);
                }), // Down
                new LittleButton(new Margin(52, 1, 1), "Right", "Set right", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    moveTelescope(event, space, 1, 0);
                }), // Right
                new LittleButton(new Margin(53, 1, 1), "Scan", "Scan signal", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    var currentCoordinates = space.getLore().split(";");
                    var signal = scanSignal(Integer.parseInt(currentCoordinates[0]), Integer.parseInt(currentCoordinates[1]));
                    if (signal != null) {
                        var player = (Player)event.getWhoClicked();
                        var progressBar = menu.getPanel("finder").getComponent("Bar", Bar.class);
                        new BukkitRunnable() {
                            int progress = 0;

                            @Override
                            public void run() {
                                if (progress < 6) {
                                    progress++;
                                    progressBar.setValue(event.getInventory(), 0, progress);
                                    if (progress == 6) cancel();
                                }
                            }
                        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, signal.getType() * 60L);

                        var signalBody = new Item("Signal", signal.getData());
                        Manager.setInteger2Container(signalBody, 1, "is_signal");
                        Manager.setInteger2Container(signalBody, signal.getType(), "signal_type");
                        Manager.giveItems(signalBody, player);
                        moveTelescope(event, space, 0, 0);
                    }
                }) // Scan signal
            ), "finder", MenuSizes.SixLines
        )
    ), "finderMenu");

    private static void moveTelescope(InventoryClickEvent event, ClickArea space, int xOperation, int yOperation) {
        var currentCoordinates = space.getLore().split(";", 2);
        var newX = Integer.parseInt(currentCoordinates[0]) + xOperation;
        var newY = Integer.parseInt(currentCoordinates[1]) + yOperation;

        drawSpace(event.getInventory(), newX, newY);
        space.setLore(newX + ";" + newY);
    }

    private static void drawSpace(Inventory inventory, int x, int y) {
        for (int i = x; i < x + 8; i++) {
            for (int j = y; j < y + 4; j++) {
                inventory.setItem(y * 9 + i, new Item("", "", Material.BLACK_STAINED_GLASS_PANE));
            }
        }

        for (var sig : signals) {
            if (sig.getY() - y >= 4 || sig.getX() - x >= 9) continue;
            inventory.setItem(sig.getY() * 9 + sig.getY(), new Item("signal", "Type: " + sig.getType(), Material.RED_STAINED_GLASS_PANE));
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
        startSignalGeneration(50, 50, 60);
        this.baseBlock = baseBlock;
    }

    @Override
    public void computerClick(Player player) {
        if (!this.isPowered) return;
        finderInterface.getPanel("finder").getView(player);
    }
}

package org.cordell.anizotti.anizottiVOTV.computer.signals;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.computer.Computer;

import org.j1sk1ss.itemmanager.manager.Manager;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.Bar;
import org.j1sk1ss.menuframework.objects.interactive.components.Button;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Direction;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.List;


public class Converter extends Computer {
    private static final MenuWindow converterInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new Bar(new Margin(3, 1, 6), Direction.Right),
                new Button(new Margin(0, 3, 3), "Decrypt", "Decrypt signal from hand", (event, menu) -> {
                    var player = (Player)event.getWhoClicked();
                    var signal = player.getInventory().getItemInMainHand();
                    if (Manager.getIntegerFromContainer(signal, "is_signal") != -1) {
                        var signalType = Manager.getIntegerFromContainer(signal, "signal_type");
                        var progressBar = menu.getPanel("converter").getComponent("Bar", Bar.class);

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
                        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, signalType * 60L);
                        Manager.setLore(signal, Signal.xorEncryptDecrypt(String.join("", Manager.getLoreLines(signal))));
                    }
                })
            ), "converter", MenuSizes.ThreeLines
        )
    ), "converterMenu");

    // Non-static logic

    public Converter(Block baseBlock) {
        this.baseBlock = baseBlock;
    }

    @Override
    public void computerClick(Player player) {
        if (!this.isPowered) return;
        converterInterface.getPanel("converter").getView(player);
    }
}

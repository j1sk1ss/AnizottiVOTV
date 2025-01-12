package org.cordell.anizotti.anizottiVOTV.computer.signals;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.computer.Computer;

import org.j1sk1ss.itemmanager.manager.Manager;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.Button;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.List;


public class Converter extends Computer {
    private static final MenuWindow converterInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new Button(new Margin(0, 2, 2), "Decrypt", "Decrypt signal from hand", (event, menu) -> {
                    var player = (Player)event.getWhoClicked();
                    var signal = player.getInventory().getItemInMainHand();
                    if (Manager.getIntegerFromContainer(signal, "is_signal") != -1) {
                        var signalType = Manager.getIntegerFromContainer(signal, "signal_type");

                        new BukkitRunnable() {
                            int progress = 0;
                            final int delay = signalType * 12;

                            @Override
                            public void run() {
                                if (progress < delay) {
                                    progress++;
                                    player.sendMessage("Progress: " + progress * (100 / delay) + "%");
                                    if (progress == delay) {
                                        Manager.setLore(signal, Signal.xorEncryptDecrypt(String.join("", Manager.getLoreLines(signal))));
                                        cancel();
                                    }
                                }
                            }
                        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, signalType * 20L);
                    }
                })
            ), "converter", MenuSizes.ThreeLines
        )
    ), "converterMenu");

    // Non-static logic

    public Converter(Block baseBlock) {
        this.baseBlock = baseBlock;
        this.isPowered = true;
    }

    @Override
    public void computerClick(Player player) {
        if (!this.isPowered) return;
        converterInterface.getPanel("converter").getView(player);
    }
}

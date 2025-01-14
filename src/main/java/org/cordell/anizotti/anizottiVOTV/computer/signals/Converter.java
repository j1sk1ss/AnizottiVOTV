package org.cordell.anizotti.anizottiVOTV.computer.signals;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.computer.Computer;

import org.cordell.anizotti.anizottiVOTV.managment.QuotaManager;
import org.j1sk1ss.itemmanager.manager.Manager;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.Button;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.List;


public class Converter extends Computer {
    public static int speed = 1;

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
                                        Manager.setInteger2Container(signal, 1, "is_decrypted");
                                        QuotaManager.completeQuota(2);
                                        cancel();
                                    }
                                }
                            }
                        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, (signalType / speed) * 20L);
                    }
                    else {
                        player.sendMessage("Take signal to hand");
                    }
                })
            ), "converter", MenuSizes.ThreeLines
        )
    ), "converterMenu");

    // Non-static logic

    public Converter(Block baseBlock) {
        this.baseBlock = baseBlock;
        this.isPowered = true;
        this.model = "converter";
    }

    @Override
    public void computerClick(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1.0f, 1.0f);
        if (!this.isPowered) {
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1.0f, 1.0f);
            player.sendMessage("Seems converter powered off...");
            return;
        }

        converterInterface.getPanel("converter").getView(player);
    }
}

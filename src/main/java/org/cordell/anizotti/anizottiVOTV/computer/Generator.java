package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.Button;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.List;


public class Generator extends Computer {
    public static Generator main = null;

    public static void generatorCrush() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (main == null) return;
                if (!main.isWork) return;
                System.out.println("Generator crush");
                main.isWork = false;
                ComputerManager.breakComputers();
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, 150L);
    }

    private static final MenuWindow serverInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new Button(new Margin(0, 2, 8), "TURN ON", "", (event, menu) -> {
                    if (main != null) {
                        main.isWork = true;
                        event.getWhoClicked().closeInventory();
                    }
                })
            ), "generator", MenuSizes.ThreeLines
        )
    ), "generatorMenu");

    // Non-static logic

    public Generator(Block baseBlock, String name) {
        this.baseBlock = baseBlock;
        this.name = name;
        isWork = true;
    }

    private boolean isWork;

    @Override
    public void computerClick(Player player) {
        if (this.isWork) return;
        serverInterface.getPanel("generator").getView(player);
    }
}

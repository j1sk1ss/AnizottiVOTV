package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.kitties.KittiesManager;
import org.cordell.anizotti.anizottiVOTV.managment.TeamManager;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.Button;
import org.j1sk1ss.menuframework.objects.interactive.components.LittleButton;
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
                ComputerManager.turnOffComputers();
                main.baseBlock.getWorld().playSound(main.baseBlock.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 0, 20L * 60 * 20);
    }

    private static final MenuWindow serverInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new Button(new Margin(0, 2, 8), "TURN ON", "", (event, menu) -> {
                    if (main != null) {
                        main.isWork = true;
                        var player = (Player)event.getWhoClicked();
                        player.closeInventory();
                        ComputerManager.turnOnComputers();
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1.0f, 1.0f);
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                    }
                }, Material.GOLD_INGOT)
            ), "generator", MenuSizes.ThreeLines, "\u10F1"
        ),
        new Panel(
            List.of(
                new LittleButton(new Margin(0, 0, 0), "Turn off", "Costs 15 energy", (event, menu) -> {
                    var player = (Player)event.getWhoClicked();
                    if (main != null) {
                        if (KittiesManager.useEnergy(15)) {
                            main.isWork = false;
                            player.closeInventory();
                            ComputerManager.turnOffComputers();
                        }
                        else {
                            player.sendMessage("You don`t have energy for this!");
                        }
                    }
                }, Material.BARRIER)
            ), "generator-kitties", MenuSizes.OneLine
        )
    ), "generatorMenu");

    // Non-static logic

    public Generator(Block baseBlock, String name) {
        this.baseBlock = baseBlock;
        this.name = name;
        isWork = true;
        this.model = "generator";
    }

    public boolean isWork;

    @Override
    public void computerClick(Player player) {
        if (this.isWork) {
            if (TeamManager.isKittie(player)) serverInterface.getPanel("generator-kitties").getView(player);
        }
        else {
            if (TeamManager.isPlayer(player)) serverInterface.getPanel("generator").getView(player);
        }
    }
}

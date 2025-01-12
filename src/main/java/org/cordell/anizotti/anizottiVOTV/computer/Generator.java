package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.Button;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.List;


public class Generator extends Computer {
    private static Generator main = null;

    private static final MenuWindow serverInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new Button(new Margin(0, 3, 8), "", "", (event, menu) -> {
                    if (main != null) main.isWork = true;
                })
            ), "server"
        )
    ), "serverMenu");

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
        serverInterface.getPanel("server").getView(player, name);
    }
}

package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.cordell.anizotti.anizottiVOTV.Utils;

import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.ClickArea;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Server extends Computer {
    private static final ArrayList<Server> servers = new ArrayList<>();

    private static final MenuWindow serverInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new ClickArea(new Margin(0, 8, 6), (event, menu) -> {
                    event.getInventory().setItem(event.getSlot(), new ItemStack(Material.GREEN_CONCRETE));
                    if (checkServerWindow(event.getInventory())) {
                        var server = getServer(Utils.getInventoryTitle(event));
                        if (server != null) {
                            server.isWork = true;
                            event.getWhoClicked().closeInventory();
                        }
                    }
                }, "server_area", "")
            ), "server"
        )
    ), "serverMenu");

    private static boolean checkServerWindow(Inventory inventory) {
        for (var item : inventory.getContents()) if (item.getType().equals(Material.RED_SAND)) return false;
        return true;
    }

    private static Server getServer(String name) {
        for (var server : servers) {
            if (Objects.equals(server.name, name)) return server;
        }

        return null;
    }

    // Non-static logic

    public Server(Block baseBlock, String name) {
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

package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.Utils;

import org.cordell.anizotti.anizottiVOTV.kitties.KittiesManager;
import org.cordell.anizotti.anizottiVOTV.managment.TeamManager;
import org.j1sk1ss.itemmanager.manager.Item;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.ClickArea;
import org.j1sk1ss.menuframework.objects.interactive.components.LittleButton;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class Server extends Computer {
    public static int durability = 4;

    public static final ArrayList<Server> servers = new ArrayList<>();

    public static void serverCrush() {
        new BukkitRunnable() {
            int loop = 0;
            @Override
            public void run() {
                if (loop >= durability) {
                    if (servers.isEmpty()) return;
                    int serverIndex = new Random().nextInt(servers.size());
                    if (!servers.get(serverIndex).isWork) return;

                    System.out.println("Server " + servers.get(serverIndex).name + " crush | Durability: " + durability + " loop: " + loop);
                    servers.get(serverIndex).baseBlock.getWorld().playSound(servers.get(serverIndex).baseBlock.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.5f, 1.0f);
                    servers.get(serverIndex).isWork = false;
                    ComputerManager.disconnectComputers(1);
                }

                loop++;
                if (loop >= durability) loop = 0;
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 100L, new Random().nextInt(4) * 20L * 60);
    }

    private static final MenuWindow serverInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new ClickArea(new Margin(0, 2, 8), (event, menu) -> {
                    var item = event.getInventory().getItem(event.getSlot());
                    var player = (Player)event.getWhoClicked();
                    if (item == null) return;

                    event.getInventory().setItem(event.getSlot(), new Item("FIXED", "", Material.GREEN_CONCRETE));
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.5f, 1.0f);
                    if (checkServerWindow(event.getInventory())) {
                        var server = getServer(Utils.getInventoryTitle(event).split(" ")[1]);
                        if (server != null) {
                            server.isWork = true;
                            player.closeInventory();
                            ComputerManager.connectComputers(1);
                            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.5f, 1.0f);
                        }
                    }
                }, "server_area", ""),
                new LittleButton(new Margin(27, 0, 0), "Update", "", (event, menu) -> {
                    for (int i = 0; i < 27; i++) {
                        if (new Random().nextBoolean()) {
                            event.getInventory().setItem(i, new Item("BAD SECTOR", "", Material.RED_SAND));
                        }
                    }
                }, Material.GOLD_INGOT)
            ), "server", MenuSizes.FourLines, "\u10F3"
        ),
        new Panel(
            List.of(
                new LittleButton(new Margin(0, 0, 0), "Destroy", "Costs 5 energy", (event, menu) -> {
                    var player = (Player)event.getWhoClicked();
                    var server = getServer(Utils.getInventoryTitle(event).split(" ")[1]);
                    if (server != null) {
                        if (KittiesManager.useEnergy(5)) {
                            server.isWork = false;
                            server.baseBlock.getWorld().playSound(server.baseBlock.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.5f, 1.0f);
                            player.closeInventory();
                            ComputerManager.disconnectComputers(1);
                        }
                        else {
                            player.sendMessage("You don`t have energy for this!");
                        }
                    }
                }, Material.BARRIER)
            ), "server-kitties", MenuSizes.OneLine
        )
    ), "serverMenu");

    private static boolean checkServerWindow(Inventory inventory) {
        for (var item : inventory.getContents()) {
            if (item == null) continue;
            if (item.getType().equals(Material.RED_SAND)) return false;
        }
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
        this.model = "server";
    }

    public boolean isWork;

    @Override
    public void computerClick(Player player) {
        if (this.isWork) {
            if (TeamManager.isKittie(player)) serverInterface.getPanel("server-kitties").getView(player, "server-kitties " + name);
        }
        else {
            if (TeamManager.isPlayer(player)) serverInterface.getPanel("server").getView(player, "server " + name);
        }
    }
}

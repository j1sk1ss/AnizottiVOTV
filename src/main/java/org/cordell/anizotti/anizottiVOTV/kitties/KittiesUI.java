package org.cordell.anizotti.anizottiVOTV.kitties;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.managment.DoorManager;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.Icon;
import org.j1sk1ss.menuframework.objects.interactive.components.LittleButton;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.List;


public class KittiesUI implements Listener {
    private static final MenuWindow kittiesInterface = new MenuWindow(List.of(
        new Panel(List.of(
            new Icon(new Margin(0, 0, 0), "Energy", "Points: " + KittiesManager.getEnergy(), Material.REDSTONE_BLOCK),
            new LittleButton(new Margin(1, 0, 0), "Invisible", "Become invisible (1 minute)\nCosts 2 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(2)) {
                    player.setInvisible(true);
                    player.setCustomNameVisible(false);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.0f);

                    Bukkit.getScheduler().runTaskLater(AnizottiVOTV.getPlugin(AnizottiVOTV.class), () -> {
                        player.setInvisible(false);
                        player.setCustomNameVisible(true);
                        player.sendMessage("You are no longer invisible!");
                    }, 60 * 20L);
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.OMINOUS_BOTTLE),
            new LittleButton(new Margin(2, 0, 0), "Meow", "Do meow", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.0f);
            }, Material.MUSIC_DISC_CAT),
            new LittleButton(new Margin(3, 0, 0), "Make level 1 key", "Costs 10 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(10)) {
                    DoorManager.giveKey(player, 1);
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.TRIAL_KEY),
            new LittleButton(new Margin(3, 0, 0), "Create stand", "Costs 6 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(6)) {
                    var location = player.getLocation();
                    var world = player.getWorld();
                    var armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
                    armorStand.setVisible(true);
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.TRIAL_KEY)
        ), "kitties-ui", MenuSizes.ThreeLines)
    ));

    public static void openKittiesMenu(Player player) {
        kittiesInterface.getPanel("kitties-ui").getComponent("Energy", Icon.class).setLore("Points: " + KittiesManager.getEnergy());
        kittiesInterface.getPanel("kitties-ui").getView(player);
    }
}

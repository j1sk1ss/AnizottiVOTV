package org.cordell.anizotti.anizottiVOTV.kitties;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;
import org.cordell.anizotti.anizottiVOTV.managment.DoorManager;

import org.j1sk1ss.itemmanager.manager.Manager;
import org.j1sk1ss.menuframework.objects.MenuSizes;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.LittleButton;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.List;


public class KittiesUI implements Listener {
    private static final MenuWindow kittiesInterface = new MenuWindow(List.of(
        new Panel(List.of(
            new LittleButton(new Margin(0, 0, 0), "Energy", "Points: ?", (event, menu) -> {
                menu.getPanel("kitties-ui").getComponent("Energy", LittleButton.class).setLore("Points: " + KittiesManager.getEnergy());
                menu.getPanel("kitties-ui").getView((Player) event.getWhoClicked());
            }, Material.REDSTONE_BLOCK),
            new LittleButton(new Margin(1, 0, 0), "Invisible", "Become invisible for 2 minutes\nCosts 2 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(2)) {
                    player.setInvisible(true);
                    player.setCustomNameVisible(false);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.0f);

                    Bukkit.getScheduler().runTaskLater(AnizottiVOTV.getPlugin(AnizottiVOTV.class), () -> {
                        player.setInvisible(false);
                        player.setCustomNameVisible(true);
                        player.sendMessage("You are no longer invisible!");
                    }, 120 * 20L);
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.OMINOUS_BOTTLE),
            new LittleButton(new Margin(2, 0, 0), "Night vision", "Night vision for 2 minute\nCosts 1 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(1)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 120, 1));
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.ENDER_EYE),
            new LittleButton(new Margin(3, 0, 0), "Regeneration", "Regeneration for 2 minute\nCosts 1 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(1)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 120, 3));
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.LINGERING_POTION),
            new LittleButton(new Margin(4, 0, 0), "Jump boost", "Jump boost for 2 minute\nCosts 1 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(1)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 20 * 120, 2));
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.IRON_BOOTS),
            new LittleButton(new Margin(5, 0, 0), "Meow", "Do meow", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.0f);
            }, Material.MUSIC_DISC_CAT),
            new LittleButton(new Margin(6, 0, 0), "Make level 1 key", "Costs 10 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(10)) {
                    DoorManager.giveKey(player, 1);
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.TRIAL_KEY),
            new LittleButton(new Margin(7, 0, 0), "Create stand", "Costs 6 energy", (event, menu) -> {
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
            }, Material.ARMOR_STAND),
            new LittleButton(new Margin(8, 0, 0), "Create fish", "Costs 1 energy", (event, menu) -> {
                var player = (Player)event.getWhoClicked();
                if (KittiesManager.useEnergy(1)) {
                    Manager.giveItems(new ItemStack(Material.COOKED_SALMON, 24), player);
                }
                else {
                    player.sendMessage("You don't have enough energy!");
                }
            }, Material.COOKED_SALMON)
        ), "kitties-ui", MenuSizes.OneLine)
    ));

    public static void openKittiesMenu(Player player) {
        kittiesInterface.getPanel("kitties-ui").getView(player);
    }
}

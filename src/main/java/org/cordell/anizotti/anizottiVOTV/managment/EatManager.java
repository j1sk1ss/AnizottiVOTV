package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.HashMap;
import java.util.Map;


public class EatManager implements Listener {
    private static final Map<Player, Map<String, Integer>> playerFoodData = new HashMap<>();

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        var player = event.getPlayer();
        if (TeamManager.isKittie(player)) return;

        var foodType = event.getItem().getType().toString();
        var foodData = playerFoodData.computeIfAbsent(player, k -> new HashMap<>());

        var overeat = foodData.getOrDefault(foodType, 0);
        if (++overeat > 48) {
            player.sendMessage("I don`t want to eat this...");
            event.setCancelled(true);
            return;
        }
        foodData.put(foodType, overeat);

        foodData.forEach((type, value) -> {
            if (!type.equals(foodType) && value > 0) {
                foodData.put(type, value - 1);
            }
        });

        playerFoodData.put(player, foodData);
        player.setFoodLevel(Math.max(player.getFoodLevel() - (overeat / 64), 0));
    }
}

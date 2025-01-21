package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class ItemManager {
    private static final HashMap<Player, List<ItemStack>> items = new HashMap<>();

    public static void takeAllItems(Player p) {
        var tempItems = new ArrayList<ItemStack>();
        Collections.addAll(tempItems, p.getInventory().getStorageContents());
        Collections.addAll(tempItems, p.getInventory().getArmorContents());
        p.getInventory().clear();
        items.put(p, tempItems);
    }

    public static void returnItems(Player p) {
        if (!items.containsKey(p)) return;
        p.getInventory().clear();
        for (var item : items.get(p)) {
            if (item != null) p.getInventory().addItem(item);
        }
    }
}

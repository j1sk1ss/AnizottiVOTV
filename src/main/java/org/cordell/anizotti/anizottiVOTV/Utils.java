package org.cordell.anizotti.anizottiVOTV;

import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.InvocationTargetException;


public class Utils {
    public static Inventory getTopInventory(InventoryEvent event) {
        try {
            var view = event.getView();
            var getTopInventory = view.getClass().getMethod("getTopInventory");
            getTopInventory.setAccessible(true);
            return (Inventory) getTopInventory.invoke(view);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getInventoryTitle(InventoryEvent event) {
        try {
            var view = event.getView();
            var getTopInventory = view.getClass().getMethod("getTitle");
            getTopInventory.setAccessible(true);
            return (String) getTopInventory.invoke(view);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
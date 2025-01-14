package org.cordell.anizotti.anizottiVOTV.common;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataType;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;


public class BlockManager {
    public static void addDataToBlock(Block block, String keyName, String value) {
        var state = block.getState();
        if (state instanceof TileState tileState) {
            var key = new NamespacedKey(AnizottiVOTV.getPlugin(AnizottiVOTV.class), keyName);
            var pdc = tileState.getPersistentDataContainer();
            pdc.set(key, PersistentDataType.STRING, value);
            tileState.update();
        }
    }

    public static String getDataFromBlock(Block block, String keyName) {
        var state = block.getState();
        if (state instanceof TileState tileState) {
            var key = new NamespacedKey(AnizottiVOTV.getPlugin(AnizottiVOTV.class), keyName);
            var pdc = tileState.getPersistentDataContainer();
            return pdc.get(key, PersistentDataType.STRING);
        }

        return null;
    }
}

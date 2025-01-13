package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.j1sk1ss.itemmanager.manager.Item;


public class JunkManager implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var brokenBlock = event.getBlock().getType();
        if (brokenBlock == Material.SUSPICIOUS_SAND || brokenBlock == Material.SUSPICIOUS_GRAVEL) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(
                    event.getBlock().getLocation(),
                    new Item("Junk", "Default junk. You can sell it!", Material.PAPER)
            );
        }
    }
}

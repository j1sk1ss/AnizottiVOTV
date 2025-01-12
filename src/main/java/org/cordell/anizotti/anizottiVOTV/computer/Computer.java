package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Computer {
    protected Block baseBlock;
    public abstract void computerClick(Player player);
}

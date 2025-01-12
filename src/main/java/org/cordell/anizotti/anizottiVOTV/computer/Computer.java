package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Computer {
    protected String name;
    protected Block baseBlock;
    protected boolean isPowered;

    public abstract void computerClick(Player player);
}

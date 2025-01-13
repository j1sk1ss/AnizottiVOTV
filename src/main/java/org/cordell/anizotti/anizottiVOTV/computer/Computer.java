package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Computer {
    public String name;
    public String model;
    public Block baseBlock;

    protected boolean isPowered;
    protected static int connectLevel;

    public abstract void computerClick(Player player);
}

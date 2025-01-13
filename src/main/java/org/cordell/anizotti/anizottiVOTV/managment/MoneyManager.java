package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.entity.Player;
import org.cordell.com.cordelldb.manager.Manager;

import java.io.IOException;

public class MoneyManager {
    private static final Manager manager = new Manager("player_moneys.txt");

    public static void addMoney(double money, Player p) throws IOException {
        var prevBalance = getMoney(p);
        manager.setDouble(p.getUniqueId().toString(), prevBalance + money);
        manager.save();
    }

    public static boolean removeMoney(double money, Player p) throws IOException {
        double prevBalance = getMoney(p);
        if (prevBalance < money) return false;
        manager.setDouble(p.getUniqueId().toString(), prevBalance - money);
        manager.save();
        return true;
    }

    public static Double getMoney(Player p) throws IOException {
        var balance = manager.getDouble(p.getUniqueId().toString());
        if (balance == -1) manager.setDouble(p.getUniqueId().toString(), 0);
        return balance;
    }
}

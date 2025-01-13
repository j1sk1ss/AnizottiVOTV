package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.scheduler.BukkitRunnable;
import org.cordell.anizotti.anizottiVOTV.AnizottiVOTV;


public class DaysManager {
    public static int day = 0;

    public static void startDayTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                newDay();
            }
        }.runTaskTimer(AnizottiVOTV.getPlugin(AnizottiVOTV.class), 20L * 60 * 5, 20L * 60 * 10);
    }

    public static void newDay() {
        day++;
    }
}

package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;


public class QuotaManager {
    private static BossBar quota;
    private static int target;

    public static void startQuota(int target) {
        QuotaManager.target = target;
        quota = Bukkit.createBossBar("Signals target: " + target, BarColor.GREEN, BarStyle.SOLID);
        quota.setProgress(0.0);

        for (var player : Bukkit.getOnlinePlayers()) {
            if (TeamManager.isKittie(player)) continue;
            quota.addPlayer(player);
        }
    }

    public static void completeQuota(int count) {
        if (quota != null) quota.setProgress((quota.getProgress() + count) / QuotaManager.target);
    }

    public static void stopQuota() {
        if (quota == null) return;
        quota.setProgress(0.0);
        quota.removeAll();
    }

    public static BossBar getQuotaBar() {
        return quota;
    }

    public static boolean isCompleteQuota() {
        if (quota == null) return true;
        return quota.getProgress() == 100.0;
    }
}

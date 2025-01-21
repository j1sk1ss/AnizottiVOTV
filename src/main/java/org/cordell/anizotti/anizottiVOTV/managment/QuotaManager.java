package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.io.IOException;


public class QuotaManager {
    private static BossBar quota;
    private static int target;
    private static int currentProgress;

    public static void startQuota(int target) {
        QuotaManager.target = target;
        QuotaManager.currentProgress = 0;
        quota = Bukkit.createBossBar("Signals target: " + target, BarColor.GREEN, BarStyle.SOLID);
        quota.setProgress(0.0);

        for (var player : Bukkit.getOnlinePlayers()) {
            if (TeamManager.isKittie(player)) continue;
            quota.addPlayer(player);
        }
    }

    public static void completeQuota(int count) {
        if (quota != null) {
            QuotaManager.currentProgress += count;
            quota.setProgress((double) QuotaManager.currentProgress / QuotaManager.target);
            if (QuotaManager.currentProgress >= QuotaManager.target) {
                for (var p : Bukkit.getOnlinePlayers()) {
                    if (!TeamManager.isPlayer(p)) continue;
                    p.sendMessage("Quota completed! Your award is 500$");
                    try {
                        MoneyManager.addMoney(500, p);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
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

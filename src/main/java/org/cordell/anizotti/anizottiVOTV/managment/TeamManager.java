package org.cordell.anizotti.anizottiVOTV.managment;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class TeamManager {
    private final static List<Player> players = new ArrayList<>();
    private final static List<Player> kitties = new ArrayList<>();

    public static void addPlayer2Players(Player p) {
        players.add(p);
    }

    public static void addPlayer2Kitties(Player p) {
        kitties.add(p);
    }

    public static void removePlayerFromPlayers(Player p) {
        players.remove(p);
    }

    public static void removePlayerFromKitties(Player p) {
        kitties.remove(p);
    }

    public static boolean isPlayer(Player p) {
        return players.contains(p);
    }

    public static boolean isKittie(Player p) {
        return kitties.contains(p);
    }

    public static void informKitties(String s) {
        for (var p : kitties) p.sendMessage(s);
    }

    public static void informPlayers(String s) {
        for (var p : players) p.sendMessage(s);
    }
}

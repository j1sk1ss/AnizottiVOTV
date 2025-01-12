package org.cordell.anizotti.anizottiVOTV.computer;

import org.bukkit.entity.Player;
import org.j1sk1ss.menuframework.objects.MenuWindow;
import org.j1sk1ss.menuframework.objects.interactive.components.ClickArea;
import org.j1sk1ss.menuframework.objects.interactive.components.LittleButton;
import org.j1sk1ss.menuframework.objects.interactive.components.Panel;
import org.j1sk1ss.menuframework.objects.nonInteractive.Margin;

import java.util.List;


public class Finder extends Computer {
    private static final int[][] spaceBody = new int[50][50];

    private static final MenuWindow finderInterface = new MenuWindow(List.of(
        new Panel(
            List.of(
                new ClickArea(new Margin(0, 4, 8), null, "space", "0;0"), // Signals
                new LittleButton(new Margin(40, 1, 1), "Up", "Set up", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    var currentCoordinates = space.getLore().split(";");
                }), // Up
                new LittleButton(new Margin(48, 1, 1), "Left", "Set left", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    var currentCoordinates = space.getLore().split(";");
                }), // Left
                new LittleButton(new Margin(49, 1, 1), "Down", "Set down", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    var currentCoordinates = space.getLore().split(";");
                }), // Down
                new LittleButton(new Margin(50, 1, 1), "Right", "Set right", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    var currentCoordinates = space.getLore().split(";");
                }), // Right
                new LittleButton(new Margin(53, 1, 1), "Scan", "Scan signal", (event, menu) -> {
                    var space = menu.getPanel("finder").getComponent("space", ClickArea.class);
                    var currentCoordinates = space.getLore().split(";");
                }) // Scan signal
            ), "finder"
        )
    ), "finderMenu");

    @Override
    public void computerClick(Player player) {
        finderInterface.getPanel("finderMenu").getView(player);
    }

    private void drawSpace(ClickArea clickArea, int x, int y) {
        for (int i = x; i < 50; i++) {
            for (int j = y; j < 50; j++) {

            }
        }
    }
}

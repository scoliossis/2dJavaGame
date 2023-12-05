package scoliosis.Libs;

import scoliosis.Display;
import scoliosis.Game;
import scoliosis.GameLibs.MoveLib;
import scoliosis.GameLibs.Velocity;
import scoliosis.Menus.LevelEditor;
import scoliosis.Menus.TitleScreen;
import scoliosis.Options.Config;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static scoliosis.Display.mainframe;
import static scoliosis.Display.pausescreen;
import static scoliosis.Game.extratimer;
import static scoliosis.Game.levelreader;
import static scoliosis.Main.maptoload;
import static scoliosis.Main.resourcesFile;

public class ScreenLib {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int width = (int) screenSize.getWidth() / 4;
    public static int height = (int) screenSize.getHeight() / 4;


    public static int getXcoord() {
        int newcoord = (int) (Math.random() * 480f);
        newcoord = Math.max(newcoord, 0);
        newcoord = Math.min(newcoord, 430);

        return newcoord;
    }

    public static int getYcoord() {
        int newcoord = (int) (Math.random() * 270f);

        newcoord = Math.max(newcoord, 0);
        newcoord = Math.min(newcoord, 170);

        return newcoord;
    }

    public static void changeScreen(String screenname) {
        if ((Display.pausescreen && screenname == "game") || (Display.ingame && screenname == "pause")) {
            Velocity.xvelocity = 0;
            Velocity.yvelocity = 0;
            if (Display.pausescreen) {
                Game.time += System.currentTimeMillis() - extratimer;
            }
            else {
                extratimer = System.currentTimeMillis();
            }
        }
        else Game.ticks = 0;

        if (Display.leveleditor) {
            if (Files.exists(Paths.get(resourcesFile + "/"+maptoload))) {
                try {
                    Files.write(Paths.get(resourcesFile + "/"+maptoload), LevelEditor.Locations.toString().getBytes(StandardCharsets.UTF_8));

                    levelreader = Files.readAllLines(Paths.get(resourcesFile + "/"+maptoload)).toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");
                    Game.levelreaderSplit = levelreader.split(",");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (screenname == "game") {
            Game.findfinish = true;
        }

        Display.titlescreen = false;
        Display.optionsmenu = false;
        Display.ingame = false;
        Display.pausescreen = false;
        Display.leveleditor = false;

        MouseLib.leftclicked = false;

        if (screenname == "title") Display.titlescreen = true;
        else if (screenname == "options") {
            Config.load();

            Display.optionsmenu = true;
        }
        else if (screenname == "game") {
            Game.loadMap(maptoload);

            TitleScreen.sillytime = System.currentTimeMillis();

            Display.ingame = true;
        }
        else if (screenname == "pause") Display.pausescreen = true;
        else if (screenname == "leveleditor") Display.leveleditor = true;
    }
}

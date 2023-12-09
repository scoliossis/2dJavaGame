package scoliosis.Libs;

import scoliosis.Display;
import scoliosis.Game;
import scoliosis.GameLibs.MoveLib;
import scoliosis.GameLibs.Velocity;
import scoliosis.Main;
import scoliosis.Menus.ChooseLevel;
import scoliosis.Menus.LevelEditor;
import scoliosis.Menus.TitleScreen;
import scoliosis.Options.Config;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static scoliosis.Display.mainframe;
import static scoliosis.Display.pausescreen;
import static scoliosis.Game.*;
import static scoliosis.Libs.RenderLib.BlurBufferedImage;
import static scoliosis.Libs.RenderLib.background;
import static scoliosis.Main.*;
import static scoliosis.Menus.ChooseLevel.map;

public class ScreenLib {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int width = (int) screenSize.getWidth() / 4;
    public static int height = (int) screenSize.getHeight() / 4;

    public static BufferedImage[] blackgrounds = new BufferedImage[3];


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

        if (screenname == "levels") {
            int length = background.length-2; // the last 2 backgrounds do not match the others and look out of place
            int randonum = ((int) (Math.random() * length));
            int randonum2 = (randonum+1 >= length ? randonum+1-length : randonum+1);
            int randonum3 = ((randonum+2 >= length ? randonum+2-length : randonum+2));

            if (randonum + 1 > highestUnlockedWorld) blackgrounds[0] = BlurBufferedImage(RenderLib.splitBufferedImage(0, 0, background[randonum].getWidth()/3, background[randonum].getHeight(), background[randonum]));
            else blackgrounds[0] = RenderLib.splitBufferedImage(0, 0, background[randonum].getWidth()/3, background[randonum].getHeight(), background[randonum]);
            if (randonum2 + 1 > highestUnlockedWorld) blackgrounds[1] = BlurBufferedImage(RenderLib.splitBufferedImage(background[randonum2].getWidth()-320, 0, background[randonum2].getWidth()/3, background[randonum2].getHeight(), background[randonum2]));
            else blackgrounds[1] = RenderLib.splitBufferedImage(background[randonum2].getWidth()-320, 0, background[randonum2].getWidth()/3, background[randonum2].getHeight(), background[randonum2]);
            if (randonum3 + 1 > highestUnlockedWorld) blackgrounds[2] = BlurBufferedImage(RenderLib.splitBufferedImage(background[randonum3].getWidth()-160, 0, background[randonum3].getWidth()/3, background[randonum3].getHeight(), background[randonum3]));
            else blackgrounds[2] = RenderLib.splitBufferedImage(background[randonum3].getWidth()-160, 0, background[randonum3].getWidth()/3, background[randonum3].getHeight(), background[randonum3]);

            ChooseLevel.campaign = false;
            ChooseLevel.practisescreen = false;
            Display.leveleditor = false;

            map = 0;
        }

        if (Display.leveleditor) {
            if (Files.exists(Paths.get(resourcesFile + "/"+lastLoadedMap))) {
                try {
                    Files.write(Paths.get(resourcesFile + "/"+lastLoadedMap), LevelEditor.Locations.toString().getBytes(StandardCharsets.UTF_8));

                    levelreader = Files.readAllLines(Paths.get(resourcesFile + "/"+lastLoadedMap)).toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");
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
        Display.chooselevel = false;

        MouseLib.leftclicked = false;

        if (screenname == "title") Display.titlescreen = true;
        else if (screenname == "levels") Display.chooselevel = true;
        else if (screenname == "options") {
            Config.load();

            Display.optionsmenu = true;
        }
        else if (screenname == "game") {

            TitleScreen.sillytime = System.currentTimeMillis();

            Display.ingame = true;
        }
        else if (screenname == "pause") Display.pausescreen = true;
        else if (screenname == "leveleditor") Display.leveleditor = true;
    }
}

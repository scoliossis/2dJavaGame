package scoliosis.Libs;

import scoliosis.Display;

import java.awt.*;

import static scoliosis.Display.mainframe;

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
        Display.titlescreen = false;
        Display.optionsmenu = false;
        Display.ingame = false;
        Display.pausescreen = false;
        Display.leveleditor = false;

        if (screenname == "title") Display.titlescreen = true;
        else if (screenname == "options") Display.optionsmenu = true;
        else if (screenname == "game") Display.ingame = true;
        else if (screenname == "pause") Display.pausescreen = true;
        else if (screenname == "leveleditor") Display.leveleditor = true;
    }
}

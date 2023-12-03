package scoliosis.Menus;

import scoliosis.Display;
import scoliosis.Game;
import scoliosis.Libs.MouseLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Options.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static scoliosis.Display.mainframe;
import static scoliosis.Game.levelreader;
import static scoliosis.Main.resourcesFile;

public class TitleScreen {

    static String[] titlebuttons = {"start", "options", "level editor"};

    static boolean leftclicked = false;

    public static double sillytime = System.currentTimeMillis();

    public static void titleScreen(BufferedImage bi, BufferStrategy bs) throws IOException {
        if (bs != null) {

            Graphics g = bs.getDrawGraphics();
            BufferedImage image = ImageIO.read(new File(resourcesFile + "/titlescreen.png"));

            g.drawImage(image, 0, 0, ScreenLib.width, ScreenLib.height, null);

            RenderLib.drawString(g, "game made by scoliosis!", 20, 30, 30, "Comic Sans MS", Font.BOLD, new Color(255,255,255));


            for (int i = 0; i < titlebuttons.length; i++) {

                if (MouseLib.isMouseOverCoords(0, 270 - 170 + (i * 40), g.getFontMetrics().stringWidth("start") + 30, 30)) {

                    RenderLib.drawString(g, titlebuttons[i], 0, 270 - 142 + (i * 40), 43, "SansSerif", Font.ITALIC, new Color(255,255,255));

                    if (leftclicked != MouseLib.leftclicked && MouseLib.leftclicked) {
                        if (i == 0) {
                            MouseLib.leftclicked = false;
                            leftclicked = false;

                            ScreenLib.changeScreen("game");

                            levelreader = "0,0,0,250,-16777216,0," + Files.readAllLines(Paths.get(resourcesFile + "/levelcreator.cfg")).toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");
                            Game.levelreaderSplit = levelreader.split(",");

                            sillytime = System.currentTimeMillis();
                        }
                        if (i == 1) {
                            MouseLib.leftclicked = false;
                            leftclicked = false;
                            ScreenLib.changeScreen("options");

                            Config.load();
                        }
                        if (i == 2) {
                            MouseLib.leftclicked = false;
                            leftclicked = false;
                            ScreenLib.changeScreen("leveleditor");
                        }

                    }
                } else {

                    RenderLib.drawString(g, titlebuttons[i], 0, 270 - 140 + (i * 40), 40, "SansSerif", Font.ITALIC, new Color(162, 159, 159));

                }
            }

            g.dispose();
            bs.show();
        }
    }
}
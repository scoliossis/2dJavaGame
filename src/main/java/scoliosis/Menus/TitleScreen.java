package scoliosis.Menus;

import scoliosis.Libs.MouseLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.ScreenLib;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static scoliosis.Game.mapworking;
import static scoliosis.Main.resourcesFile;

public class TitleScreen {

    static String[] titlebuttons = {"start", "options", "level editor"};

    static boolean leftclicked = false;

    public static double sillytime = System.currentTimeMillis();
    public static boolean leveleditor = false;

    public static void titleScreen(BufferedImage bi, BufferStrategy bs) throws IOException {
        if (bs != null) {

            Graphics g = bs.getDrawGraphics();

            g.drawImage(RenderLib.getBufferedImage("titlescreen"), 0, 0, ScreenLib.width, ScreenLib.height, null);

            //RenderLib.drawString(g, "game made by scoliosis!", 20, 30, 30, "Comic Sans MS", Font.BOLD, new Color(255,255,255));


            for (int i = 0; i < titlebuttons.length; i++) {

                if (MouseLib.isMouseOverCoords(0, 270 - 170 + (i * 40), 300, 30)) {

                    RenderLib.drawString(g, titlebuttons[i], 0, 270 - 142 + (i * 40), 43, "SansSerif", Font.ITALIC, new Color(255,255,255));

                    if (leftclicked != MouseLib.leftclicked && MouseLib.leftclicked) {
                        leveleditor = false;
                        if (i == 0) {
                            if (mapworking) ScreenLib.changeScreen("levels");
                        }
                        if (i == 1) {
                            ScreenLib.changeScreen("options");
                        }
                        if (i == 2) {
                            ScreenLib.changeScreen("levels");
                            ChooseLevel.practisescreen = true;
                            leveleditor = true;
                        }

                    }
                    leftclicked = MouseLib.leftclicked;
                } else {

                    RenderLib.drawString(g, titlebuttons[i], 0, 270 - 140 + (i * 40), 40, "SansSerif", Font.ITALIC, new Color(162, 159, 159));

                }
            }

            g.dispose();
            bs.show();
        }
    }
}

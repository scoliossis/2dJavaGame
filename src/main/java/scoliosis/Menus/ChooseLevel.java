package scoliosis.Menus;

import scoliosis.Game;
import scoliosis.Libs.KeyLib;
import scoliosis.Libs.MouseLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Main;

import javax.management.monitor.MonitorSettingException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static scoliosis.Libs.RenderLib.*;
import static scoliosis.Libs.ScreenLib.blackgrounds;
import static scoliosis.Menus.TitleScreen.leveleditor;

public class ChooseLevel {

    public static String[] options = {"campaign", "practise", "custom"};

    public static boolean campaign = true;

    public static int map = 0;
    public static boolean practisescreen = false;
    public static int page = 0;
    static boolean clickedleft = false;

    public static void LevelSelector(BufferedImage bi, BufferStrategy bs) {

        if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
            if (practisescreen && !leveleditor) practisescreen = false;
            else ScreenLib.changeScreen("title");
        }

        if (bs != null) {

            Graphics g = bs.getDrawGraphics();

            if (!practisescreen) {
                for (int i = 0; i < blackgrounds.length; i++) {
                    if (!MouseLib.isMouseOverCoords(i * 160, 0, 160, 270)) {
                        RenderLib.drawImage(i * 160, 0, 160, 270, blackgrounds[i], g);
                        RenderLib.drawCenteredString(g, options[i], i * 160 + 80, 150, 30, "Comic Sans MS", 1, new Color(255, 255, 255));
                    }
                }
                for (int i = 0; i < blackgrounds.length; i++) {
                    if (MouseLib.isMouseOverCoords(i * 160, 0, 160, 270)) {
                        RenderLib.drawImage(i * 160 - 10, -10, 180, 290, blackgrounds[i], g);
                        RenderLib.drawCenteredString(g, options[i], i * 160 + 80, 150, 33, "Comic Sans MS", 1, new Color(255, 255, 255));

                        if (MouseLib.leftclicked) {
                            switch (i) {
                                case 0:
                                    campaign = true;

                                    if (map == 0) {
                                        Game.lives = 3;

                                        Game.loadMap(sortedCampaignLevels[map]);
                                        map++;
                                    }
                                    ScreenLib.changeScreen("game");


                                case 1:
                                    practisescreen = true;
                                    page = 0;
                            }
                        }
                    }
                }
            }
            else if (practisescreen) {
                if (page+1 > Main.highestUnlockedWorld && !leveleditor) {
                    if (page > 5) {
                        RenderLib.drawImage(0,0,480,270, RenderLib.getBufferedImage("locked"), g);
                    }
                    else RenderLib.drawImage(0,0,480, 270, BlurBufferedImage(background[page]), g);
                }

                else RenderLib.drawImage(0,0,480, 270, background[page], g);

                if (page != 2 && ((page+1 <= Main.highestUnlockedWorld || page <= 5) || leveleditor)) RenderLib.drawString(g, "world: " + (page+1), 10, 30, 20, "Comic Sans MS", 1, new Color(0,0,0));
                else if (page <= 5) RenderLib.drawString(g, "world: " + (page+1), 10, 30, 20, "Comic Sans MS", 1, new Color(255, 255, 255));
                else RenderLib.drawString(g, "world: ??", 10, 30, 20, "Comic Sans MS", 1, new Color(255, 255, 255));

                if ((page+1 <= Main.highestUnlockedWorld || page <= 5) || leveleditor) RenderLib.drawRect(105, 145, 255, 10, new Color(0,0,0), g);

                for (int i = 1; i <= 4; i++) {
                    if ((page+1 <= Main.highestUnlockedWorld || page <= 5) || leveleditor) {
                        if (MouseLib.isMouseOverCoords(95 * i - 30, 130, 40, 40))
                            RenderLib.drawCircle(95 * i - 35, 125, 50, 50, new Color(63 * i - 1, 255 - (63 * i - 1), 0), g);
                        else
                            RenderLib.drawCircle(95 * i - 30, 130, 40, 40, new Color(63 * i - 1, 255 - (63 * i - 1), 0), g);

                        if ((page + 1 > Main.highestUnlockedWorld || page + 1 == Main.highestUnlockedWorld && i > Main.highestUnlockedLevel) && !leveleditor) {
                            if (MouseLib.isMouseOverCoords(95 * i - 30, 130, 40, 40))
                                RenderLib.drawImage(95 * i - 35, 125, 50, 50, RenderLib.getBufferedImage("lock"), g);
                            else RenderLib.drawImage(95 * i - 30, 130, 40, 40, RenderLib.getBufferedImage("lock"), g);
                        }

                        else {
                            if (MouseLib.isMouseOverCoords(95 * i - 30, 130, 40, 40) && MouseLib.leftclicked && !clickedleft) {
                                Game.loadMap((page+1) + "-" + (i) + ".map");
                                if (!leveleditor) ScreenLib.changeScreen("game");
                                else ScreenLib.changeScreen("leveleditor");
                            }
                        }
                    }
                }

                // left arrow
                if (page > 0) {

                    RenderLib.drawRect(400, 177, 15, 16, new Color(32, 32, 32, 100), g);

                    if (MouseLib.isMouseOverCoords(400, 177, 15, 16)) {
                        RenderLib.drawPoligon(new int[]{410, 402, 410}, new int[]{180, 185, 190}, new Color(255, 0, 0), g);

                        if (MouseLib.leftclicked && !clickedleft) {
                            page--;
                        }
                    }

                    else RenderLib.drawPoligon(new int[]{410, 405, 410}, new int[]{180, 185, 190}, new Color(255, 0, 0), g);
                }

                // right arrow
                if (page < 7) {
                    RenderLib.drawRect(420, 177, 15, 16, new Color(32,32,32, 100), g);

                    if (MouseLib.isMouseOverCoords(420, 177, 15, 16)) {
                        RenderLib.drawPoligon(new int[]{425, 433, 425}, new int[]{180, 185, 190}, new Color(0, 255, 0), g);

                        if (MouseLib.leftclicked && !clickedleft) {
                            page++;
                        }
                    }

                    else RenderLib.drawPoligon(new int[]{425, 430, 425}, new int[]{180, 185, 190}, new Color(0, 255, 0), g);
                }

            }

            clickedleft = MouseLib.leftclicked;
            g.dispose();
            bs.show();
        }
    }
    public static String[] sortedCampaignLevels;

    public static ArrayList allLevels = new ArrayList();


    public static void getLevels() {
        if (Files.exists(Paths.get(Main.resourcesFile))) {
            for (File file : Paths.get(Main.resourcesFile).toFile().listFiles()) {
                if (file.getName().endsWith(".map")) {
                    allLevels.add(file.getName());
                }
            }

            sortedCampaignLevels = allLevels.toString().split(",");

            sortedCampaignLevels = Arrays.toString(sortedCampaignLevels).replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "").split(",");
        }
    }
}

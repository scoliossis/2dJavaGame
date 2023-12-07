package scoliosis.Menus;

import scoliosis.Game;
import scoliosis.Libs.KeyLib;
import scoliosis.Libs.MouseLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static scoliosis.Libs.ScreenLib.blackgrounds;

public class ChooseLevel {

    public static String[] options = {"campaign", "practise", "custom"};

    public static boolean campaign = true;

    public static int map = 0;

    public static void LevelSelector(BufferedImage bi, BufferStrategy bs) {

        if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
            ScreenLib.changeScreen("title");
        }

        if (bs != null) {

            Graphics g = bs.getDrawGraphics();

            for (int i = 0; i < blackgrounds.length; i++) {
                if (!MouseLib.isMouseOverCoords(i*160, 0, 160, 270)) {
                    RenderLib.drawImage(i * 160, 0, 160, 270, blackgrounds[i], g);
                    RenderLib.drawCenteredString(g, options[i], i*160+80, 150, 30, "Comic Sans MS", 1, new Color(255,255,255));
                }
            }
            for (int i = 0; i < blackgrounds.length; i++) {
                if (MouseLib.isMouseOverCoords(i*160, 0, 160, 270)) {
                    RenderLib.drawImage(i*160 - 10, -10, 180, 290, blackgrounds[i], g);
                    RenderLib.drawCenteredString(g, options[i], i*160+80, 150, 33, "Comic Sans MS", 1, new Color(255,255,255));

                    if (MouseLib.leftclicked) {
                        switch (i) {
                            case 0:
                                campaign = true;

                                if (map == 0) {
                                    Game.loadMap(sortedCampaignLevels[map]);
                                    map++;
                                }

                        }
                        Game.lives = 3;

                        ScreenLib.changeScreen("game");
                    }
                }
            }
            g.dispose();
            bs.show();
        }
    }

    static void SortAlphabetically(String []s) {
        for (int i=1 ;i<s.length; i++) {
            String temp = s[i];

            int j = i - 1;
            while (j >= 0 && s[j].compareToIgnoreCase(temp) < 0) {
                s[j+1] = s[j];
                j--;
            }
            s[j+1] = temp;
        }
    }

    public static String[] sortedCampaignLevels;

    public static ArrayList allLevels = new ArrayList();

    static {
        if (Files.exists(Paths.get(Main.resourcesFile))) {
            for (File file : Paths.get(Main.resourcesFile).toFile().listFiles()) {
                if (file.getName().endsWith(".map")) {
                    allLevels.add(file.getName());
                }
            }

            sortedCampaignLevels = allLevels.toString().split(",");

            SortAlphabetically(sortedCampaignLevels);
            sortedCampaignLevels = Arrays.toString(sortedCampaignLevels).replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "").split(",");
        }
    }
}

package scoliosis;

import scoliosis.GameLibs.MoveLib;
import scoliosis.Libs.KeyLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Menus.ChooseLevel;
import scoliosis.Menus.LevelEditor;
import scoliosis.Menus.OptionsScreen;
import scoliosis.Options.Configs;
import scoliosis.Options.Setting;
import scoliosis.Options.Settings.BooleanSetting;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;

import static scoliosis.GameLibs.MoveLib.*;
import static scoliosis.GameLibs.Velocity.*;
import static scoliosis.Libs.RenderLib.background;
import static scoliosis.Libs.ScreenLib.height;
import static scoliosis.Libs.ScreenLib.width;
import static scoliosis.Main.*;
import static scoliosis.Menus.ChooseLevel.map;
import static scoliosis.Menus.ChooseLevel.sortedCampaignLevels;
import static scoliosis.Menus.LevelEditor.testing;


public class Game {
    public static String levelreader = "";
    public static String[] levelreaderSplit = "".split(",");

    public static double fps = 0;
    static double timebeforetick = 0;
    static double timeaftertick = 0;


    public static int charecterheight = 25;
    public static int charecterwidth = 25;

    static int xtodraw = 0;
    static float fakex = 0;

    public static Color colorofball = new Color(105, 105, 252);
    public static Color outsidering = new Color(0, 0, 0);
    public static double timespent = 0;
    public static double time = System.currentTimeMillis();
    public static int ticks = 0;

    public static double extratimer = 0;

    public static double fastesttime = 0d;

    static boolean wrotefile = false;

    public static boolean respawning = true;
    public static boolean findfinish = true;

    static double leftovertime = 0;
    static double timelast = System.currentTimeMillis();

    static int fpslockmax = 5;

    public static int lives = 3;

    public static double overalltimetaken = 0d;
    static Graphics g = null;
    static BufferStrategy publicBuffer;
    static double millisbetweentick = timebeforetick - timeaftertick;

    static int fakefakex = 0;

    public static int[] doneCoinCooridnates = new int[6];

    public static int coins = 0;

    public static void game(BufferedImage bi, BufferStrategy bs) throws IOException {
        if (bs != null) {
            g = bs.getDrawGraphics();
            publicBuffer = bs;

            if (checkDiedCampaign());
            else if (checkBeatCampaign());
            else {
                getFPS();

                drawGame();

            }
        }
    }

    static void getColorOfBall() {
        colorofball = new Color(105, 105, 252);
        outsidering = new Color(0, 0, 0);
        if (sprinting) {
            colorofball = new Color(18, 166, 255);
            outsidering = new Color(255, 255, 255);
        }
    }
    static void getCharacterHeight() {
        if (yvelocity < -2) {
            charecterheight = 20;
        } else if (yvelocity > 2) {
            charecterheight = 30;
        } else if (sprinting && !onGround) {
            charecterheight = 23;
        } else {
            charecterheight = 25;
        }
    }

    static void drawGameBackground() {
        if (ChooseLevel.campaign) g.drawImage(background[Integer.parseInt(sortedCampaignLevels[map-1].substring(0,1)) - 1], 0, 0, ScreenLib.width, ScreenLib.height, null);
        else {
            try {
                g.drawImage(background[Integer.parseInt(lastLoadedMap.substring(0, 1))-1], 0, 0, ScreenLib.width, ScreenLib.height, null);
            } catch (NumberFormatException e) {
                g.drawImage(background[0], 0, 0, ScreenLib.width, ScreenLib.height, null);
            }
        }
    }

    static void respawnScreen() {
        if (respawning) {
            if (findfinish) {
                findfinish = false;
                for (int i = 0; i < levelreaderSplit.length; i += 6) {
                    if (Integer.parseInt(levelreaderSplit[i + 5]) == 4) {
                        fakex = Integer.parseInt(levelreaderSplit[i]);
                        i = levelreaderSplit.length;
                    }
                }
            }

            xcoordinate = spawnx;
            ycoordinate = spawny;
            xvelocity = 0;
            yvelocity = 0;
            win = false;
            ticks = 0;

            if (fakex - xcoordinate < 100) respawning = false;
        }
    }

    static void ShouldKillPlayer() {
        if (ycoordinate > 300 || (KeyLib.keyPressed(KeyEvent.VK_R)) && !(ChooseLevel.campaign && win)) {
            KillPlayer();
        }
    }

    static void setCameraPosition() {
        if (xcoordinate != fakex) {
            if (xvelocity > 0) {
                if (xcoordinate != 100) fakex += (xcoordinate - fakex) * 0.1f;
                else fakex -= 0.1f;
            } else if (xvelocity < 0) {
                if (xcoordinate != 200) fakex += (xcoordinate - fakex) * 0.1f;
                else fakex += 0.1f;

            } else {
                fakex += (xcoordinate - fakex) * 0.05f;
            }

        }

        fakefakex = (int) fakex;

    }

    public static boolean overFPSlock() {
        leftovertime = (System.currentTimeMillis() - timelast) - fpslockmax;

        if (leftovertime >= fpslockmax) leftovertime -= fpslockmax;

        return leftovertime >= fpslockmax;
    }

    static void drawLevelExceptBackLayer() {
        for (int i = 0; i < levelreaderSplit.length; i += 6) {
            if (Integer.parseInt(levelreaderSplit[i + 5]) != 1) {
                xtodraw = Integer.parseInt(levelreaderSplit[i]) - fakefakex + 75;
                if (xtodraw <= 480 && xtodraw + Integer.parseInt(levelreaderSplit[i + 2]) >= 0) {

                    if (Integer.parseInt(levelreaderSplit[i + 4]) == 26) {

                        boolean found = false;
                        for (int n = 0; n < doneCoinCooridnates.length; n+=2) {
                            if (doneCoinCooridnates[n] == Integer.parseInt(levelreaderSplit[i]) && doneCoinCooridnates[n+1] == Integer.parseInt(levelreaderSplit[i + 1])) {
                                found = true;
                            }
                        }

                        if (found) RenderLib.drawImage(xtodraw, Integer.parseInt(levelreaderSplit[i + 1]), Integer.parseInt(levelreaderSplit[i + 2]), Integer.parseInt(levelreaderSplit[i + 3]), RenderLib.getBufferedImage("coin3"), g);
                        else RenderLib.drawImage(xtodraw, Integer.parseInt(levelreaderSplit[i + 1]), Integer.parseInt(levelreaderSplit[i + 2]), Integer.parseInt(levelreaderSplit[i + 3]), RenderLib.getBufferedImage("coin1"), g);

                    }

                    else
                        RenderLib.drawImage(xtodraw, Integer.parseInt(levelreaderSplit[i + 1]), Integer.parseInt(levelreaderSplit[i + 2]), Integer.parseInt(levelreaderSplit[i + 3]), Integer.parseInt(levelreaderSplit[i + 4]), g);
                }
            }
        }
    }
    static void drawPlayer() {

        // player "sprite" (circle)
        RenderLib.drawCircle(75 + (xcoordinate - fakefakex), ycoordinate - 25, charecterwidth, charecterheight, colorofball, g);
        RenderLib.drawCircleOutline(75 + (xcoordinate - fakefakex), ycoordinate - 25, charecterwidth, charecterheight, outsidering, g);

        // eyes position
        int leftEyeStartX = 75 + (xcoordinate - fakefakex) + (charecterwidth / 2) - charecterwidth / 4 - 2;
        int rightEyeStartX = leftEyeStartX + charecterwidth / 2;

        int eyeStartY = ycoordinate - 25 + charecterwidth / 4;

        int eyeWidth = charecterwidth / 4;
        int eyeHeight = charecterheight / 4;

        // eyes draw
        RenderLib.drawCircle(leftEyeStartX, eyeStartY, eyeWidth, eyeHeight, new Color(255,255,255, 220), g);
        RenderLib.drawCircle(rightEyeStartX, eyeStartY, eyeWidth, eyeHeight, new Color(255,255,255, 220), g);


        // pupils position
        int distanceFromEyeCentreX = charecterwidth / 12;
        int distanceFromEyeCentreY = charecterheight / 12;

        double pupilOffsetX = 0;
        if (xvelocity > 1 || xvelocity < -1) {
            pupilOffsetX = Math.max(-distanceFromEyeCentreX, Math.min(((xvelocity / 20 * 360) * Math.PI / 180) / 3, distanceFromEyeCentreX * 2));
        }

        double pupilOffsetY = 0;
        if (yvelocity > 1 || yvelocity < 1) {
            pupilOffsetY = Math.max(-distanceFromEyeCentreY, Math.min((((-yvelocity / 10 * 360) * Math.PI / 180)) / 10 + charecterheight / 12d, distanceFromEyeCentreY * 2));
        }

        int leftPupilStartX = (int) ((leftEyeStartX + distanceFromEyeCentreX) + pupilOffsetX);
        int rightPupilStartX = (int) ((rightEyeStartX + distanceFromEyeCentreY) + pupilOffsetX);

        int pupilStartY = (int) (eyeStartY + pupilOffsetY);


        // pupils draw
        RenderLib.drawCircle(leftPupilStartX, pupilStartY, eyeWidth / 2, eyeHeight / 2, new Color(0,0,0, 220), g);
        RenderLib.drawCircle(rightPupilStartX, pupilStartY, eyeWidth / 2, eyeHeight / 2, new Color(0,0,0, 220), g);


        // mouth
        int mouthHeight = (int) (Math.max(eyeHeight * Math.cos(ticks/75d), 1));

        if (xvelocity > 10 || xvelocity < -10) {
            mouthHeight = eyeHeight;
        }

        RenderLib.drawCircleOutline(75 + (xcoordinate - fakefakex) + (charecterwidth / 2) - (charecterwidth / 8), ycoordinate - 25 + charecterwidth / 2, eyeWidth, mouthHeight, new Color(0,0,0), g);


    }



    static void drawLevelBackLayer() {
        for (int i = 0; i < levelreaderSplit.length; i += 6) {
            if (Integer.parseInt(levelreaderSplit[i + 5]) == 1) {
                xtodraw = Integer.parseInt(levelreaderSplit[i]) - fakefakex + 75;
                if (xtodraw <= 480 && xtodraw + Integer.parseInt(levelreaderSplit[i + 2]) >= 0) {
                    RenderLib.drawImage(xtodraw, Integer.parseInt(levelreaderSplit[i + 1]), Integer.parseInt(levelreaderSplit[i + 2]), Integer.parseInt(levelreaderSplit[i + 3]), Integer.parseInt(levelreaderSplit[i + 4]), g);
                }
            }
        }
    }
    static void writeNewPB() throws IOException {
        Main.getCompletedLevels();

        if (!wrotefile) {
            wrotefile = true;

            String readfile;
            String whattowrite = lastLoadedMap + ":" + timespent + ",";

            if (Files.exists(Paths.get(baseName + "/times.scolio"))) {
                readfile = Files.readAllLines(Paths.get(baseName + "/times.scolio")).toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");
                if (readfile.contains(lastLoadedMap)) {
                    String[] splittext = readfile.split(",");
                    for (int i = 0; i < splittext.length; i++) {
                        if (splittext[i].contains(lastLoadedMap + ":")) {

                            if (Float.parseFloat(splittext[i].split(":")[1]) > timespent) {
                                System.out.println("previous record: " + Double.parseDouble(splittext[i].split(":")[1]));
                                whattowrite = readfile.replace(splittext[i] + ",", "") + lastLoadedMap + ":" + timespent + ",";
                                fastesttime = timespent;
                            } else {
                                whattowrite = readfile;
                                fastesttime = Double.parseDouble(splittext[i].split(":")[1]);
                            }

                            i = splittext.length;
                        }
                    }
                } else {
                    System.out.println("first time completing map!");
                    whattowrite = readfile + whattowrite;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(baseName + "/times.scolio"));
            writer.write(whattowrite);
            writer.close();
        }
    }

    static void doWinScreen() throws IOException {
        if (win) {
            time = System.currentTimeMillis();
            if (ChooseLevel.campaign) {
                ycoordinate -= 4;
                if (yvelocity < 1) yvelocity = 0f;
                xvelocity += 1f;
            } else {
                xvelocity = 0;
                yvelocity=0;
            }

            writeNewPB();

            normalWinScreen();

            campaignNextLevelCheck();

        }
    }
    static void normalWinScreen() {
        RenderLib.drawCenteredString(g, "gg! completed in " + timespent, 230, 130, 30, "Comic Sans MS", 0, new Color(0, 0, 0));
        RenderLib.drawCenteredString(g, "personal best: " + fastesttime + (fastesttime == timespent ? "(NEW PB)" : ""), 230, 170, 30, "Comic Sans MS", 1, new Color(227, 0, 174, 255));

    }
    static void campaignNextLevelCheck() {
        if (ChooseLevel.campaign && xvelocity > 10) {
            win = false;

            if (Configs.hardMode || Configs.hardcore) lives += 1;
            else lives += 2;
            overalltimetaken += timespent;
            KillPlayer();

            System.out.println("starting next level");

            loadMap(sortedCampaignLevels[ChooseLevel.map]);
            ChooseLevel.map++;

        }
    }

    static void tickGame() {
        ticks++;
        if (ticks == 1) {
            respawnScreen();
            time = System.currentTimeMillis();
        }

        if (!win) {
            timespent = ((System.currentTimeMillis() - time) / 1000);
            wrotefile = false;
        }
    }

    static void drawHUD() {
        //RenderLib.drawString(g, "fps: " + Math.round(fps), 380, 30, 10, "Comic Sans MS", 0, new Color(0, 0, 0));

        RenderLib.drawString(g, new DecimalFormat("#.###").format(timespent), 5, 20, 13, "Comic Sans MS", 0, new Color(0, 0, 0));

        if (ChooseLevel.campaign)
            if (!Configs.hardcore) RenderLib.drawString(g, "lives: " + lives, 5, 50, 13, "Comic Sans MS", 1, new Color(255, 0, 136));

        RenderLib.drawImage(420, 10, 15, 15, RenderLib.getBufferedImage("coin3"), g);
        RenderLib.drawImage(437, 10, 15, 15, RenderLib.getBufferedImage("coin3"), g);
        RenderLib.drawImage(454, 10, 15, 15, RenderLib.getBufferedImage("coin3"), g);

        for (int i = 0; i < coins; i++) {
            RenderLib.drawImage(419 + i*17, 9, 16, 16, RenderLib.getBufferedImage("coin1"), g);
        }

        Iterator allsettings = OptionsScreen.settings.iterator();

        int setnum = 0;
        int setnum2 = 0;

        while (allsettings.hasNext()) {
            Setting setting = (Setting) allsettings.next();

            if (setting instanceof BooleanSetting) {
                if ((Boolean) setting.get(Boolean.class)) {
                    RenderLib.drawImage(25 * setnum2, 250, 20, 20, RenderLib.getBufferedImage(challenges[setnum]), g);

                    setnum2++;
                }
            }

            setnum++;
        }


    }

    public static void drawGame() throws IOException {

        if (overFPSlock()) {
            tickGame();

            drawGameBackground();

            if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
                if (!testing) ScreenLib.changeScreen("pause");
                else ScreenLib.changeScreen("leveleditor");
            }


            getColorOfBall();

            ShouldKillPlayer();

            getCharacterHeight();

            respawnScreen();

            setCameraPosition();

            drawLevelBackLayer();
            drawPlayer();
            drawLevelExceptBackLayer();

            doWinScreen();

            drawHUD();

            finishDrawing();

            timelast = System.currentTimeMillis();

            MoveLib.MoveLibChecks();
        }
    }

    static void finishDrawing() {
        g.dispose();
        publicBuffer.show();
    }

    public static void getFPS() {
        timebeforetick = System.nanoTime();

        millisbetweentick = timebeforetick - timeaftertick;

        timeaftertick = System.nanoTime();


        // so it doesnt check too often.
        if (System.currentTimeMillis() % 75 == 0)

            // gets the amount of seconds between each frame, then divide 1 by it.
            fps = 1 / (millisbetweentick / 1000000000);

    }

    public static boolean checkBeatCampaign() {
        if (ChooseLevel.campaign && map >= sortedCampaignLevels.length && win) {
            RenderLib.drawCenteredString(g, "WELL DONE!", 240, 120, 40, "Comic Sans MS", 1, new Color(164, 0, 255, 255));
            RenderLib.drawCenteredString(g, "time taken: " + new DecimalFormat("#.###").format(overalltimetaken + timespent), 240, 160, 40, "Comic Sans MS", 1, new Color(255, 0, 253, 255));

            if (KeyLib.keyPressed(KeyEvent.VK_R)) {
                map = 0;
                ScreenLib.changeScreen("levels");
            }

            finishDrawing();
        }

        return ChooseLevel.campaign && map >= sortedCampaignLevels.length && win;
    }

    public static boolean checkDiedCampaign() {
        if (ChooseLevel.campaign && lives <= 0) {
            RenderLib.drawCenteredString(g, "good night (press r)", 240, 120, 40, "Comic Sans MS", 1, new Color(255, 0, 0, 140));
            map = 0;

            if (KeyLib.keyPressed(KeyEvent.VK_R)) {
                ScreenLib.changeScreen("levels");
            }

            finishDrawing();
        }

        return ChooseLevel.campaign && lives <= 0;
    }

    public static void KillPlayer() {
        if (!(win && ChooseLevel.campaign)) {
            if (Configs.hardcore) lives = 0;

            xcoordinate = spawnx;
            ycoordinate = spawny;
            xvelocity = 0;
            yvelocity = 0;
            win = false;
            ticks = 0;
            respawning = true;
            lives -= 1;
            resetCoins();
        }
    }

    public static boolean win = false;

    public static void win() {
        if (coins == 3 || !Configs.collector) {
            win = true;
            findfinish = true;
            xvelocity = 0;

            resetCoins();

            if (testing) {
                ScreenLib.changeScreen("leveleditor");
                testing = false;

                xcoordinate = spawnx;
                ycoordinate = spawny;
                xvelocity = 0;
                yvelocity = 0;
                win = false;
                ticks = 0;
                respawning = true;
            }
        }
        else {
            KillPlayer();
        }
    }

    public static boolean mapworking = true;
    public static String lastLoadedMap = "";
    public static void loadMap(String mapname) {
        lastLoadedMap = mapname;

        if (Files.exists(Paths.get(resourcesFile + "/" + mapname))) {
            try {
                levelreader = Files.readAllLines(Paths.get(resourcesFile + "/"+mapname)).toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");
                if (!levelreader.isEmpty()) {
                    mapworking = true;
                    Game.levelreaderSplit = levelreader.split(",");
                    LevelEditor.Locations.clear();
                    for (int p = 0; p < Game.levelreaderSplit.length; p++) {
                        LevelEditor.Locations.add(Integer.parseInt(Game.levelreaderSplit[p]));
                    }
                }
                else {
                    mapworking = false;
                    System.out.println("current level is empty!");

                    LevelEditor.Locations.clear();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("map not found!");
            try {
                new File(resourcesFile + "/" + mapname).createNewFile();
                LevelEditor.Locations.clear();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void resetCoins() {
        doneCoinCooridnates = new int[6];
        coins = 0;
    }
}

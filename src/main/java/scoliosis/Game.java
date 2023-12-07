package scoliosis;

import scoliosis.GameLibs.MoveLib;
import scoliosis.Libs.KeyLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Menus.ChooseLevel;
import scoliosis.Menus.LevelEditor;

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

import static scoliosis.GameLibs.MoveLib.*;
import static scoliosis.GameLibs.Velocity.*;
import static scoliosis.Libs.RenderLib.background;
import static scoliosis.Libs.ScreenLib.height;
import static scoliosis.Main.*;
import static scoliosis.Menus.ChooseLevel.map;
import static scoliosis.Menus.ChooseLevel.sortedCampaignLevels;
import static scoliosis.Menus.LevelEditor.testing;


public class Game {

    // dirty brown
    //150, 60, 30
    // grassy green
    // 58, 100, 21

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

    public static void game(BufferedImage bi, BufferStrategy bs) throws IOException {
        if (bs != null) {
            Graphics g = bs.getDrawGraphics();

            if (ChooseLevel.campaign && lives <= 0) {
                RenderLib.drawCenteredString(g, "good night (press r)", 240, 120, 40, "Comic Sans MS", 1, new Color(255, 0, 0, 140));
                map = 0;

                if (KeyLib.keyPressed(KeyEvent.VK_R)) {
                    ScreenLib.changeScreen("levels");
                }

                g.dispose();
                bs.show();
            }

            else if (ChooseLevel.campaign && map >= sortedCampaignLevels.length && win) {
                RenderLib.drawCenteredString(g, "WELL DONE!", 240, 120, 40, "Comic Sans MS", 1, new Color(164, 0, 255, 255));

                if (KeyLib.keyPressed(KeyEvent.VK_R)) {
                    map = 0;
                    ScreenLib.changeScreen("levels");
                }

                g.dispose();
                bs.show();
            }


            else {
                timebeforetick = System.nanoTime();

                double millisbetweentick = timebeforetick - timeaftertick;

                timeaftertick = System.nanoTime();


                // so it doesnt check too often.
                if (System.currentTimeMillis() % 75 == 0)

                    // gets the amount of seconds between each frame, then divide 1 by it.
                    fps = 1 / (millisbetweentick / 1000000000);


                leftovertime = (System.currentTimeMillis() - timelast) - fpslockmax;

                while (leftovertime >= fpslockmax) {
                    leftovertime -= fpslockmax;
                    ticks++;
                    if (ticks == 1) {
                        time = System.currentTimeMillis();
                    }

                    if (ChooseLevel.campaign) g.drawImage(background[Integer.parseInt(sortedCampaignLevels[map-1].substring(0,1)) - 1], 0, 0, ScreenLib.width, ScreenLib.height, null);
                    else g.drawImage(background[0], 0, 0, ScreenLib.width, ScreenLib.height, null);

                    if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
                        if (!testing) ScreenLib.changeScreen("pause");
                        else ScreenLib.changeScreen("leveleditor");
                    }


                    colorofball = new Color(105, 105, 252);
                    outsidering = new Color(0, 0, 0);
                    if (sprinting) {
                        colorofball = new Color(18, 166, 255);
                        outsidering = new Color(255, 255, 255);
                    }


                    // CHECKING IF DIED
                    if (ycoordinate > 270 || (KeyLib.keyPressed(KeyEvent.VK_R)) && !(ChooseLevel.campaign && win)) {
                        KillPlayer();
                    }

                    if (yvelocity < -2) {
                        charecterheight = 20;
                    } else if (yvelocity > 2) {
                        charecterheight = 30;
                    } else if (sprinting && !onGround) {
                        charecterheight = 23;
                    } else {
                        charecterheight = 25;
                    }

                    //g.drawImage(bi, 0, 0, ScreenLib.width, ScreenLib.height, null);

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

                    int fakefakex = (int) fakex;
                    int texnum = 0;

                    for (int i = 0; i < levelreaderSplit.length; i += 6) {
                        if (Integer.parseInt(levelreaderSplit[i + 5]) == 1) {
                            xtodraw = Integer.parseInt(levelreaderSplit[i]) - fakefakex + 75;
                            if (xtodraw <= 480 && xtodraw + Integer.parseInt(levelreaderSplit[i + 2]) >= 0) {
                                RenderLib.drawImage(xtodraw, Integer.parseInt(levelreaderSplit[i + 1]), Integer.parseInt(levelreaderSplit[i + 2]), Integer.parseInt(levelreaderSplit[i + 3]), Integer.parseInt(levelreaderSplit[i + 4]), g);
                            }

                        }
                    }

                    RenderLib.drawCircle(75 + (xcoordinate - fakefakex), ycoordinate - 25, charecterwidth, charecterheight, colorofball, g);
                    RenderLib.drawCircleOutline(75 + (xcoordinate - fakefakex), ycoordinate - 25, charecterwidth, charecterheight, outsidering, g);

                    RenderLib.drawString(g, "fps: " + Math.round(fps), 380, 30, 10, "Comic Sans MS", 0, new Color(0, 0, 0));

                    for (int i = 0; i < levelreaderSplit.length; i += 6) {
                        if (Integer.parseInt(levelreaderSplit[i + 5]) != 1) {
                            xtodraw = Integer.parseInt(levelreaderSplit[i]) - fakefakex + 75;
                            if (xtodraw <= 480 && xtodraw + Integer.parseInt(levelreaderSplit[i + 2]) >= 0) {
                                RenderLib.drawImage(xtodraw, Integer.parseInt(levelreaderSplit[i + 1]), Integer.parseInt(levelreaderSplit[i + 2]), Integer.parseInt(levelreaderSplit[i + 3]), Integer.parseInt(levelreaderSplit[i + 4]), g);
                            }
                        }
                    }
                    if (win) {
                        time = System.currentTimeMillis();
                        if (ChooseLevel.campaign) {
                            if (yvelocity < 1) yvelocity = 1f;
                            xvelocity += 0.2f;
                        } else xvelocity = 0;

                        if (!wrotefile) {
                            wrotefile = true;

                            String readfile;
                            String whattowrite = maptoload + ":" + timespent + ",";

                            if (Files.exists(Paths.get(resourcesFile + "/times.scolio"))) {
                                readfile = Files.readAllLines(Paths.get(resourcesFile + "/times.scolio")).toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");
                                if (readfile.contains(maptoload)) {
                                    String[] splittext = readfile.split(",");
                                    for (int i = 0; i < splittext.length; i++) {
                                        if (splittext[i].contains(maptoload + ":")) {

                                            if (Float.parseFloat(splittext[i].split(":")[1]) > timespent) {
                                                System.out.println("previous record: " + Double.parseDouble(splittext[i].split(":")[1]));
                                                whattowrite = readfile.replace(splittext[i] + ",", "") + maptoload + ":" + timespent + ",";
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

                            BufferedWriter writer = new BufferedWriter(new FileWriter(resourcesFile + "/times.scolio"));
                            writer.write(whattowrite);
                            writer.close();
                        }


                        RenderLib.drawCenteredString(g, "gg! completed in " + timespent, 230, 130, 30, "Comic Sans MS", 0, new Color(0, 0, 0));
                        RenderLib.drawCenteredString(g, "personal best: " + fastesttime + (fastesttime == timespent ? "(NEW PB)" : ""), 230, 170, 30, "Comic Sans MS", 1, new Color(227, 0, 174, 255));

                        if (ChooseLevel.campaign && xvelocity > 10) {
                            win = false;
                            lives += 2;
                            KillPlayer();


                            loadMap(sortedCampaignLevels[ChooseLevel.map]);
                            ChooseLevel.map++;

                        }
                    } else {
                        timespent = ((System.currentTimeMillis() - time) / 1000);
                        wrotefile = false;
                    }

                    RenderLib.drawString(g, new DecimalFormat("#.###").format(timespent), 5, 20, 13, "Comic Sans MS", 0, new Color(0, 0, 0));

                    if (ChooseLevel.campaign)
                        RenderLib.drawString(g, "lives: " + lives, 5, 50, 13, "Comic Sans MS", 1, new Color(255, 0, 136));


                    g.dispose();
                    bs.show();
                    timelast = System.currentTimeMillis();

                    MoveLib.MoveLibChecks();
                }
            }
        }
    }

    public static void KillPlayer() {
        if (!(win && ChooseLevel.campaign)) {
            xcoordinate = spawnx;
            ycoordinate = spawny;
            xvelocity = 0;
            yvelocity = 0;
            win = false;
            ticks = 0;
            respawning = true;
            lives -= 1;
        }
    }

    public static boolean win = false;

    public static void win() {
        win = true;
        findfinish = true;
        xvelocity = 0;

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

    public static boolean mapworking = true;
    public static void loadMap(String mapname) {
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
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("map not found!");
            try {
                new File(resourcesFile + "/" + mapname).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

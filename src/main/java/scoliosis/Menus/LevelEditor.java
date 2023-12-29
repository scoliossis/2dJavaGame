package scoliosis.Menus;

import scoliosis.Display;
import scoliosis.Game;
import scoliosis.Libs.KeyLib;
import scoliosis.Libs.MouseLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Main;
import scoliosis.Options.Config;
import scoliosis.Options.Setting;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static scoliosis.Game.charecterwidth;
import static scoliosis.GameLibs.MoveLib.*;
import static scoliosis.GameLibs.Velocity.*;
import static scoliosis.Libs.MouseLib.*;
import static scoliosis.Libs.RenderLib.background;
import static scoliosis.Libs.RenderLib.getBufferedImage;
import static scoliosis.Main.*;

public class LevelEditor {


    static boolean mouseclicked = false;
    static boolean rightclicker = false;

    static int leftclickedposy = 0;
    static int rightclickedposy = 0;

    public static ArrayList Locations = new ArrayList<Integer>();

    public static boolean showtopbar = true;

    public static int boxsize = 10;
    public static int blocktype = 0;

    public static int xoffset = 0;

    public static boolean testing = false;

    public static String testmoves = "";


    public static int zoomed = 10;

    static boolean askreset = false;


    static int blocktip = 0;

    public static int block = 0;

    static double leftovertime = 0;
    static double timelast = System.currentTimeMillis();

    public static int shapeMode = 0;
    public static int[] leftclickedpos = new int[2];

    static int shapeX = 0;
    static int shapeY = 0;
    static int shapeW = 0;
    static int shapeH = 0;

    static boolean lockedonMove = false;

    static String[] blockTypes = {"normal", "background", "foreground", "spike", "goal", "coin"};
    static boolean blockTypesShow = false;

    static float theWide = 66;
    static float maxSize = 20;
    static float minSize = 1;

    static float totalSize = maxSize - minSize;
    static float widthDivAmount = theWide / totalSize;

    static float sizeAmount = boxsize * widthDivAmount + minSize;

    static boolean acter = false;

    public static void LevelEditor(BufferedImage bi, BufferStrategy bs) throws IOException {
        leftovertime = (System.currentTimeMillis() - timelast) - 20;

        if (leftovertime >= 20) {
            leftovertime -= 20;
            if (bs != null) {

                Graphics g = bs.getDrawGraphics();
                g.drawImage(background[1], 0, 0, ScreenLib.width, ScreenLib.height, null);

                if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
                    if (askreset) askreset = false;
                    else ScreenLib.changeScreen("title");
                    testing = false;
                }

                if (!askreset && KeyLib.keyPressed(KeyEvent.VK_R)) {
                    askreset = true;
                }
                if (KeyLib.keyPressed(KeyEvent.VK_P)) {
                    testmoves = "";
                }

                if (KeyLib.keyPressed(KeyEvent.VK_T)) {
                    if (!Locations.isEmpty()) {
                        ScreenLib.changeScreen("game");
                        testing = true;
                        xcoordinate = spawnx;
                        ycoordinate = spawny;
                        xvelocity = 0;
                        yvelocity = 0;
                        testmoves = "";
                    } else System.out.println("level is empty bud!");
                }


                // bugged :(
            /*
            if (KeyLib.keyPressed(KeyEvent.VK_S)) {
                zoomed-=1;
            }
            if (KeyLib.keyPressed(KeyEvent.VK_W)) {
                zoomed+=1;
            }
             */

                if (boxsize != 1) {
                    for (int i = (int) (xoffset % (boxsize * zoomed / 10f)); i < 480; i += (int) (boxsize * (zoomed / 10f))) {
                        RenderLib.drawLine(i, 0, i, 270, new Color(32, 32, 32), g);
                    }
                    for (int i = 0; i < 270; i += (int) (boxsize * (zoomed / 10f))) {
                        RenderLib.drawLine(0, i, 480, i, new Color(32, 32, 32), g);
                    }
                }

                int toremove = 0;

                if (leftclicked && !mouseclicked) {
                    leftclickedposy = realmouseycoord();
                }
                if (rightclicked && !rightclicker) {
                    rightclickedposy = realmouseycoord();
                }

                if (((leftclicked && mouseclicked && leftclickedposy > 40) || (rightclicked && rightclicker && rightclickedposy > 40)) && realmouseycoord() <= 40) {
                    acter = true;
                    showtopbar = false;
                }
                if (!leftclicked && mouseclicked && acter) {
                    acter = false;
                    showtopbar = true;
                }

                if (KeyLib.isKeyDown(KeyEvent.VK_A)) {
                    xoffset += 15;
                }

                if (KeyLib.isKeyDown(KeyEvent.VK_D)) {
                    xoffset -= 15;
                }

                if (((leftclicked && !mouseclicked) || (rightclicked && !rightclicker)) && shapeMode != 0) {
                    leftclickedpos[0] = mousexcoord(boxsize);
                    leftclickedpos[1] = mouseycoord(boxsize);
                }

                if (leftclicked && !mouseclicked && MouseLib.isMouseOverCoords((int) (spawnx * (zoomed / 10f) + xoffset), (int) (spawny - (25 * (zoomed / 10f))), (int) (25 * (zoomed / 10f)), (int) (25 * (zoomed / 10f)))) {
                    lockedonMove = true;
                }
                else if (!leftclicked && mouseclicked) {
                    lockedonMove = false;
                }

                if (leftclicked && lockedonMove) {
                    spawnx = (int) (MouseLib.mousexcoord(1) * (zoomed / 10f) - xoffset) - 12;
                    spawny = (int) (MouseLib.mouseycoord(1) - (25 * (zoomed / 10f))) +  25;
                }


                if (shapeMode >= 1 && ((leftclicked && mouseclicked) || (rightclicked && rightclicker))) {
                    int xco = (int) ((int) ((int) ((((leftclickedpos[0] * boxsize - xoffset) / (zoomed / 10f)) + (xoffset % boxsize)))) * (zoomed / 10f) + xoffset);
                    int wid = (MouseLib.mousexcoord(boxsize)-leftclickedpos[0]) * boxsize + boxsize;
                    int yco = leftclickedpos[1] * boxsize;
                    int hei = (MouseLib.mouseycoord(boxsize) - leftclickedpos[1]) * boxsize + boxsize;


                    for (int x = Math.min(wid/boxsize, 0); x < Math.max(wid/boxsize, 0); x++) {
                        for (int y = Math.min(hei/boxsize, 0); y < Math.max(hei/boxsize, 0); y++) {
                            shapeX = xco;
                            shapeY = yco;
                            shapeW = wid;
                            shapeH = hei;

                            if (leftclicked) RenderLib.drawImage(x*boxsize + xco, y*boxsize + yco, boxsize, boxsize, block, g);
                        }
                    }

                    if (rightclicker) RenderLib.drawOutline(xco, yco, wid, hei, new Color(255,0,0), g);

                }

                if (shapeMode >= 1) {
                    if (!leftclicked && mouseclicked) {
                        for (int x = Math.min(shapeW, 0); x < Math.max(shapeW, 0); x += boxsize) {
                            for (int y = Math.min(shapeH, 0); y < Math.max(shapeH, 0); y += boxsize) {

                                boolean founded = false;
                                for (int i = 0; i < Locations.size(); i += 6) {
                                    if ((int) Locations.get(i) == shapeX + x - xoffset && (int) Locations.get(i + 1) == shapeY + y) {
                                        if (shapeMode == 2) {
                                            Locations.remove(i);
                                            Locations.remove(i);
                                            Locations.remove(i);
                                            Locations.remove(i);
                                            Locations.remove(i);
                                            Locations.remove(i);
                                        }
                                        else founded = true;
                                    }
                                }

                                if (!founded) {
                                    Locations.add(shapeX + x - xoffset);
                                    Locations.add(shapeY + y);
                                    Locations.add((int) (1 * boxsize / (zoomed / 10f)));
                                    Locations.add((int) (1 * boxsize / (zoomed / 10f)));

                                    Locations.add(block);
                                    Locations.add(blocktype);
                                }
                            }
                        }
                    } else if (!rightclicked && rightclicker) {
                        for (int x = 0; x < shapeW; x += boxsize) {
                            for (int y = 0; y < shapeH; y += boxsize) {
                                for (int i = 0; i < Locations.size(); i += 6) {
                                    if ((int) Locations.get(i) == shapeX + x - xoffset && (int) Locations.get(i + 1) == shapeY + y) {
                                        Locations.remove(i);
                                        Locations.remove(i);
                                        Locations.remove(i);
                                        Locations.remove(i);
                                        Locations.remove(i);
                                        Locations.remove(i);
                                    }
                                }
                            }
                        }
                    }
                }



                if ((leftclicked || rightclicked) && (realmouseycoord() >= 35 || !showtopbar) && shapeMode == 0 && !lockedonMove && !blockTypesShow
                        && !MouseLib.isMouseOverCoords(430, 0, 16, 10) && !MouseLib.isMouseOverCoords(430, 30, 16, 10)) {

                    boolean dontadd = false;
                    for (int i = 0; i < Locations.size(); i += 6) {
                        if ((int) Locations.get(i) == (int) ((((mousexcoord(boxsize) * boxsize - xoffset) / (zoomed / 10f)) + (xoffset % boxsize)))) {
                            if ((int) Locations.get(i + 1) == mouseycoord(boxsize) * boxsize) {
                                dontadd = true;
                                toremove = i;
                                if (leftclicked) {
                                    if ((Integer) Locations.get(i + 4) != block) {
                                        Locations.remove(toremove);
                                        Locations.remove(toremove);
                                        Locations.remove(toremove);
                                        Locations.remove(toremove);
                                        Locations.remove(toremove);
                                        Locations.remove(toremove);

                                        Locations.add((int) ((((mousexcoord(boxsize) * boxsize - xoffset) / (zoomed / 10f)) + (xoffset % boxsize))));
                                        Locations.add(mouseycoord(boxsize) * boxsize);
                                        Locations.add((int) (1 * boxsize / (zoomed / 10f)));
                                        Locations.add((int) (1 * boxsize / (zoomed / 10f)));

                                        Locations.add(block);
                                        Locations.add(blocktype);
                                    }
                                }
                            }
                        }
                    }

                    if (!dontadd && leftclicked) {
                        Locations.add((int) ((((mousexcoord(boxsize) * boxsize - xoffset) / (zoomed / 10f)) + (xoffset % boxsize))));
                        Locations.add(mouseycoord(boxsize) * boxsize);
                        Locations.add((int) (1 * boxsize / (zoomed / 10f)));
                        Locations.add((int) (1 * boxsize / (zoomed / 10f)));

                        Locations.add(block);
                        Locations.add(blocktype);

                    }

                    if (rightclicked && dontadd) {
                        // x
                        Locations.remove(toremove);
                        // y
                        Locations.remove(toremove);
                        // w
                        Locations.remove(toremove);
                        // h
                        Locations.remove(toremove);
                        // block
                        Locations.remove(toremove);
                        // block type
                        Locations.remove(toremove);

                    }
                }



                RenderLib.drawCircle((int) (spawnx * (zoomed / 10f) + xoffset), (int) (spawny - (25 * (zoomed / 10f))), (int) (25 * (zoomed / 10f)), (int) (25 * (zoomed / 10f)), new Color(160, 250, 239), g);

                for (int i = 0; i < Locations.size(); i += 6) {
                    if ((int) ((int) Locations.get(i) * (zoomed / 10f) + xoffset) <= 480 && (int) ((int) Locations.get(i) * (zoomed / 10f) + xoffset) + (int) (((int) Locations.get(i + 2) * 1f) * (zoomed / 10f)) >= 0) {
                        RenderLib.drawImage((int) ((int) Locations.get(i) * (zoomed / 10f) + xoffset), ((int) Locations.get(i + 1)), (int) (((int) Locations.get(i + 2) * 1f) * (zoomed / 10f)), (int) ((float) ((int) Locations.get(i + 3) * 1f) * (zoomed / 10f)), (int) Locations.get(i + 4), g);
                        RenderLib.drawString(g, String.valueOf((int) Locations.get(i + 5)), (int) ((int) Locations.get(i) * (zoomed / 10f) + xoffset) + 3, ((int) Locations.get(i + 1)) + 10, 10, "Comic Sans MS", 0, new Color(0, 0, 0));
                    }
                }

                if (showtopbar) {
                    RenderLib.drawRect(0, 0, 480, 35, new Color(255, 255, 255), g);
                    RenderLib.drawLine(0, 35, 480, 35, new Color(95, 131, 224), g);

                    RenderLib.drawRect(5, 22, 27, 10, new Color(215, 216, 229), g);
                    RenderLib.drawOutline(5, 22, 27, 10, new Color(48, 51, 63), g);
                    RenderLib.drawPoligon(new int[]{7, 15, 7}, new int[]{22, 27, 32}, new Color(50, 124, 50), g);
                    RenderLib.drawString(g, "try", 16, 31, 11, "Comic Sans MS", 0, new Color(0, 0, 0));
                    if (isMouseOverCoords(5, 22, 27, 10) && leftclicked && !mouseclicked) {
                        if (!Locations.isEmpty()) {
                            ScreenLib.changeScreen("game");
                            testing = true;
                            xcoordinate = spawnx;
                            ycoordinate = spawny;
                            xvelocity = 0;
                            yvelocity = 0;
                            testmoves = "";
                        } else System.out.println("empty level, please stop!");
                    }

                    // shapes
                    RenderLib.drawRect(40, 3, 100, 26, new Color(215, 216, 229), g);
                    RenderLib.drawOutline(40, 3, 100, 26, new Color(48, 51, 63), g);


                    if (shapeMode == 1) RenderLib.drawOutline(45, 5, 10, 10, new Color(255, 0, 0), g);
                    else RenderLib.drawOutline(45, 5, 10, 10, new Color(0, 0, 0), g);

                    if (shapeMode == 2) RenderLib.drawRect(60, 5, 10, 10, new Color(255, 0, 0), g);
                    else RenderLib.drawRect(60, 5, 10, 10, new Color(0, 0, 0), g);

                    if (leftclicked && !mouseclicked) {
                        if (MouseLib.isMouseOverCoords(45, 5, 10, 10)) shapeMode = 1;
                        else if (MouseLib.isMouseOverCoords(60, 5, 10, 10)) shapeMode = 2;
                    }



                    // grid size
                    RenderLib.drawRect(142, 2, 66, 14, new Color(215, 216, 229), g);
                    RenderLib.drawOutline(142, 2, 66, 14, new Color(48, 51, 63), g);
                    RenderLib.drawString(g, "grid size: " + boxsize, 144, 10, 9, "Comic Sans MS", 0, new Color(0, 0, 0));

                    if (leftclicked && isMouseOverCoords(142, 2, 66, 14)) {
                        sizeAmount = MouseLib.mousexcoord(1) - 142f;
                        boxsize = (int) ((int) (sizeAmount / widthDivAmount) + minSize);
                    }

                    RenderLib.drawRect(142, 14, (Math.min((int) sizeAmount, 66)), 2, new Color(200, 20, 240), g);


                    // block type changer
                    RenderLib.drawRect(142, 18, 66, 14, new Color(215, 216, 229), g);
                    RenderLib.drawString(g, blockTypes[blocktype], 143, 29, 12, "Comic Sans MS", 0, new Color(0, 0, 0));

                    if (blockTypesShow) {
                        for (int i = 0; i < blockTypes.length; i++) {
                            RenderLib.drawRect(142, 15 + (15 * (i+1)), 66, 16, new Color(215, 216, 229), g);
                            RenderLib.drawString(g, blockTypes[i], 143, 15 + (15 * (i+1)) + 12, 12, "Comic Sans MS", 0, new Color(0,0,0));

                            if (leftclicked && !mouseclicked && MouseLib.isMouseOverCoords(142, 15 + (15 * (i+1)), 66, 16)) {
                                blocktype = i;
                                blockTypesShow = false;
                            }
                        }
                    }

                    if (isMouseOverCoords(142, 18, 66, 14)) {
                        if (leftclicked && !mouseclicked) blockTypesShow = !blockTypesShow;
                    }

                    if (!blockTypesShow) RenderLib.drawOutline(142, 18, 66, 14, new Color(48, 51, 63), g);
                    else RenderLib.drawOutline(142, 18, 66, 13 + (15 * (blockTypes.length)), new Color(48, 51, 63), g);



                    RenderLib.drawRect(430, 35, 16, 10, new Color(215, 216, 229), g);
                    RenderLib.drawPoligon(new int[]{434, 438, 442}, new int[]{43, 37, 43}, new Color(48, 51, 63), g);

                    if (MouseLib.isMouseOverCoords(430, 35, 16, 10) && leftclicked && !mouseclicked) {
                        showtopbar = false;
                    }

                    RenderLib.drawRect(210, 3, 210, 26, new Color(215, 216, 229), g);
                    RenderLib.drawOutline(210, 3, 210, 26, new Color(48, 51, 63), g);

                    for (int i = 0; i < textures.length; i++) {
                        int xcoord = (215 + (i * 12) - (204 * (i / 17)));
                        int ycoord = 5 + (12 * (i / 17));
                        RenderLib.drawImage(xcoord, ycoord, 10, 10, i, g);
                        if (i == block) {
                            RenderLib.drawOutline(xcoord, ycoord, 10, 10, new Color(238, 4, 78), g);
                        }

                        if (isMouseOverCoords(xcoord, ycoord, 10, 10) && leftclicked && !mouseclicked) {
                            shapeMode = 0;
                            block = i;
                        }
                    }

                }


                else {
                    RenderLib.drawRect(430, 0, 16, 10, new Color(215, 216, 229), g);
                    RenderLib.drawPoligon(new int[]{434, 438, 442}, new int[]{2, 8, 2}, new Color(48, 51, 63), g);

                    if (MouseLib.isMouseOverCoords(430, 0, 16, 10) && leftclicked && !mouseclicked) {
                        showtopbar = true;
                    }
                }

                if (testmoves != "") {
                    String[] testsplit = testmoves.split(",");
                    for (int i = 0; i < testsplit.length; i += 2) {
                        RenderLib.drawCircle((int) (Integer.parseInt(testsplit[i]) * (zoomed / 10f) + xoffset), (int) (Integer.parseInt(testsplit[i + 1]) - (25 * (zoomed / 10f))), (int) (charecterwidth * (zoomed / 10f)), (int) (charecterwidth * (zoomed / 10f)), new Color(93, 124, 124, 61), g);
                        RenderLib.drawCircleOutline((int) (Integer.parseInt(testsplit[i]) * (zoomed / 10f) + xoffset), (int) (Integer.parseInt(testsplit[i + 1]) - (25 * (zoomed / 10f))), (int) (charecterwidth * (zoomed / 10f)), (int) (charecterwidth * (zoomed / 10f)), new Color(0, 0, 0, 61), g);
                    }
                }

                if (askreset) {
                    RenderLib.drawRect(0, 0, 480, 270, new Color(0, 0, 0), g);
                    RenderLib.drawCenteredString(g, "ARE YOU SURE YOU WANT TO RESET LEVEL?", 200, 70, 15, "Comic Sans MS", 0, new Color(255, 255, 255));

                    RenderLib.drawRect(40, 130, 170, 40, new Color(115, 255, 0), g);
                    RenderLib.drawRect(220, 130, 170, 40, new Color(255, 0, 0), g);
                    RenderLib.drawString(g, "press \"ESCAPE\" to not", 45, 150, 15, "Comic Sans MS", 0, new Color(0, 0, 0));
                    RenderLib.drawString(g, "press \"R\" again to reset", 225, 150, 15, "Comic Sans MS", 0, new Color(0, 0, 0));

                    if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
                        askreset = false;
                    }
                    if (KeyLib.keyPressed(KeyEvent.VK_R)) {
                        askreset = false;
                        Locations.clear();
                        testmoves = "";
                    }
                }

                g.dispose();
                bs.show();
            }

            timelast = System.currentTimeMillis();

            mouseclicked = leftclicked;
            rightclicker = rightclicked;
        }

    }

}

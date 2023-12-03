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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static scoliosis.Display.mainframe;
import static scoliosis.Game.*;
import static scoliosis.GameLibs.MoveLib.*;
import static scoliosis.GameLibs.Velocity.*;
import static scoliosis.Libs.MouseLib.*;
import static scoliosis.Main.resourcesFile;

public class LevelEditor {


    static boolean mouseclicked = false;
    static boolean rightclicker = false;

    static int leftclickedposy = 0;
    static int rightclickedposy = 0;

    public static ArrayList Locations = new ArrayList<Integer>();

    public static boolean showtopbar = true;

    public static int redcolor = 0;
    public static int greencolor = 0;
    public static int bluecolor = 0;

    public static int boxsize = 10;
    public static int blocktype = 0;

    public static int xoffset = 0;

    public static boolean testing = false;

    public static String testmoves = "";

    public static boolean showgrid = true;

    public static int zoomed = 10;

    static boolean askreset = false;

    static boolean colorpicking = false;

    static int blocktip = 0;

    public static void LevelEditor(BufferedImage bi, BufferStrategy bs) throws IOException {
        if (bs != null) {

            Graphics g = bs.getDrawGraphics();
            BufferedImage image = ImageIO.read(new File(resourcesFile + "/background.png"));
            g.drawImage(image, 0, 0, ScreenLib.width, ScreenLib.height, null);

            if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
                if (askreset) askreset = false;
                else ScreenLib.changeScreen("title");
                testing = false;
            }

            if (!askreset && KeyLib.keyPressed(KeyEvent.VK_R)) {
                askreset = true;
                //Locations.clear();
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
                }
                else System.out.println("level is empty bud!");
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

            if (showgrid) {
                for (int i = (int) (xoffset % (boxsize * zoomed/10f)); i < 480; i += (int) (boxsize*(zoomed / 10f))) {
                    RenderLib.drawLine(i, 0, i, 270, new Color(32, 32, 32), g);
                }
                for (int i = 0; i < 270; i += (int) (boxsize*(zoomed / 10f))) {
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
                showtopbar = false;
            }
            else {
                showtopbar = true;
            }

            if (KeyLib.isKeyDown(KeyEvent.VK_A)) {
                xoffset += 1;
            }

            if (KeyLib.isKeyDown(KeyEvent.VK_D)) {
                xoffset -= 1;
            }

            Color hoveringcolor = new Color(0,0,0);
            if ((leftclicked || rightclicked) && (realmouseycoord() >= 35 || !showtopbar)) {
                boolean dontadd = false;
                for (int i = 0; i < Locations.size(); i += 6) {
                    if ((int) Locations.get(i) == (int) ((((mousexcoord(boxsize) * boxsize-xoffset)  / (zoomed / 10f)) + (xoffset%boxsize)))) {
                        if ((int) Locations.get(i + 1) == mouseycoord(boxsize) * boxsize) {
                            dontadd = true;
                            toremove = i;
                            if (leftclicked && !colorpicking) {
                                if (new Color((Integer) Locations.get(i+4)) != new Color(redcolor, greencolor, bluecolor)) {
                                    Locations.remove(toremove);
                                    Locations.remove(toremove);
                                    Locations.remove(toremove);
                                    Locations.remove(toremove);
                                    Locations.remove(toremove);
                                    Locations.remove(toremove);

                                    Locations.add((int) ((((mousexcoord(boxsize) * boxsize-xoffset)  / (zoomed / 10f)) + (xoffset%boxsize))));
                                    Locations.add(mouseycoord(boxsize) * boxsize);
                                    Locations.add((int) (1 * boxsize / (zoomed / 10f)));
                                    Locations.add((int) (1 * boxsize / (zoomed / 10f)));

                                    Locations.add(new Color(redcolor, greencolor, bluecolor).getRGB());
                                    Locations.add(blocktype);
                                }
                            }

                            if (colorpicking) {
                                hoveringcolor = new Color((Integer) Locations.get(i + 4));
                                blocktip = (int) Locations.get(i + 5);
                            }
                        }
                    }
                }

                if (leftclicked == !mouseclicked && leftclicked && colorpicking) {
                    colorpicking = false;
                    redcolor = hoveringcolor.getRed();
                    greencolor = hoveringcolor.getGreen();
                    bluecolor = hoveringcolor.getBlue();
                    blocktype = blocktip;
                }

                if (!dontadd && leftclicked) {
                    Locations.add((int) ((((mousexcoord(boxsize) * boxsize-xoffset)  / (zoomed / 10f)) + (xoffset%boxsize))));
                    Locations.add(mouseycoord(boxsize) * boxsize);
                    Locations.add((int) (1 * boxsize / (zoomed / 10f)));
                    Locations.add((int) (1 * boxsize / (zoomed / 10f)));

                    Locations.add(new Color(redcolor, greencolor, bluecolor).getRGB());
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
                    // color
                    Locations.remove(toremove);
                    // block type
                    Locations.remove(toremove);

                }
            }

            RenderLib.drawCircle((int) (50*(zoomed / 10f)+xoffset), (int) (200 - (25 * (zoomed / 10f))), (int) (25*(zoomed / 10f)), (int) (25*(zoomed / 10f)), new Color(160, 250, 239), g);

            for (int i = 0; i < Locations.size(); i+=6) {
                RenderLib.drawRect((int) ((int) Locations.get(i) * (zoomed / 10f) +xoffset), ((int) Locations.get(i+1)), (int) (((int) Locations.get(i+2)*1f) * (zoomed / 10f)), (int) ((float) ((int) Locations.get(i+3)*1f) * (zoomed / 10f)), new Color((Integer) Locations.get(i+4)), g);
                RenderLib.drawString(g, String.valueOf((int) Locations.get(i+5)), (int) ((int) Locations.get(i) * (zoomed / 10f) +xoffset)+3, ((int) Locations.get(i+1))+10, 10, "Comic Sans MS", 0, new Color(0, 0, 0));
                //RenderLib.drawOutline((int) Locations.get(i) * 10, (int) Locations.get(i+1) * 10, 10, 10, new Color(255,255,255), g);
            }

            if (showtopbar) {
                RenderLib.drawRect(0, 0, 480, 35, new Color(255, 255, 255), g);
                RenderLib.drawLine(0, 35, 480, 35, new Color(95, 131, 224), g);

                RenderLib.drawRect(5, 22, 27, 10, new Color(215, 216, 229), g);
                RenderLib.drawOutline(5, 22, 27, 10, new Color(48, 51, 63), g);
                RenderLib.drawPoligon(new int[] {7, 15, 7}, new int[] {22, 27, 32}, new Color(50, 124, 50), g);
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
                    }
                    else System.out.println("empty level, please stop!");
                }

                RenderLib.drawRect(10, 5, 40, 14, new Color(215, 216, 229), g);
                RenderLib.drawOutline(10, 5, 40, 14, new Color(48, 51, 63), g);
                RenderLib.drawString(g, "R: " + redcolor, 12, 17, 12, "Comic Sans MS", 0, new Color(0, 0, 0));

                if (isMouseOverCoords(10, 5, 40, 14)) {
                    redcolor = colorTextBox(redcolor);
                }

                RenderLib.drawRect(60, 5, 40, 14, new Color(215, 216, 229), g);
                RenderLib.drawOutline(60, 5, 40, 14, new Color(48, 51, 63), g);
                RenderLib.drawString(g, "G: " + greencolor, 62, 17, 12, "Comic Sans MS", 0, new Color(0, 0, 0));

                if (isMouseOverCoords(60, 5, 40, 14)) {
                    greencolor = colorTextBox(greencolor);
                }

                RenderLib.drawRect(110, 5, 40, 14, new Color(215, 216, 229), g);
                RenderLib.drawOutline(110, 5, 40, 14, new Color(48, 51, 63), g);
                RenderLib.drawString(g, "B: " + bluecolor, 112, 17, 12, "Comic Sans MS", 0, new Color(0, 0, 0));

                if (isMouseOverCoords(110, 8, 40, 14)) {
                    bluecolor = colorTextBox(bluecolor);
                }

                RenderLib.drawRect(70, 20, 10, 10, new Color(redcolor, greencolor, bluecolor), g);
                if (!colorpicking) RenderLib.drawOutline(70, 20, 10, 10, new Color(48, 51, 63), g);
                else RenderLib.drawOutline(70, 20, 10, 10, new Color(255, 0, 0), g);
                if (isMouseOverCoords(70, 20, 10, 10) && leftclicked != mouseclicked && leftclicked) {
                    colorpicking = true;
                }

                RenderLib.drawRect(170, 2, 30, 14, new Color(215, 216, 229), g);
                RenderLib.drawOutline(170, 2, 30, 14, new Color(48, 51, 63), g);
                RenderLib.drawString(g, "S: " + (showgrid ? boxsize : "0"), 172, 14, 12, "Comic Sans MS", 0, new Color(0, 0, 0));

                if (isMouseOverCoords(170, 2, 30, 14)) {
                    boxsize = onenumreplace(boxsize, 0, 10);
                    if (onenumreplace(boxsize, 0, 10) != 10) showgrid = true;
                    if (boxsize == 1) boxsize = 10;
                    if (boxsize == 0) {
                        showgrid = !showgrid;
                        boxsize = 10;
                    }
                }

                RenderLib.drawRect(170, 18, 30, 14, new Color(215, 216, 229), g);
                RenderLib.drawOutline(170, 18, 30, 14, new Color(48, 51, 63), g);
                RenderLib.drawString(g, "T: " + blocktype, 172, 30, 12, "Comic Sans MS", 0, new Color(0, 0, 0));

                if (isMouseOverCoords(170, 18, 30, 14)) {
                    blocktype = onenumreplace(blocktype, 0, 4);
                }
            }

            if (testmoves != "") {
                String[] testsplit = testmoves.split(",");
                for (int i = 0; i < testsplit.length; i+=2) {
                    RenderLib.drawCircle((int) (Integer.parseInt(testsplit[i])*(zoomed / 10f)+xoffset), (int) (Integer.parseInt(testsplit[i+1]) - (25 * (zoomed / 10f))), (int) (charecterwidth*(zoomed / 10f)), (int) (charecterwidth*(zoomed / 10f)), new Color(93, 124, 124, 61), g);
                    RenderLib.drawCircleOutline((int) (Integer.parseInt(testsplit[i])*(zoomed / 10f)+xoffset), (int) (Integer.parseInt(testsplit[i+1]) - (25 * (zoomed / 10f))), (int) (charecterwidth*(zoomed / 10f)), (int) (charecterwidth*(zoomed / 10f)), new Color(0, 0, 0, 61), g);
                }
            }

            if (askreset) {
                RenderLib.drawRect(0,0,480,270, new Color(0,0,0), g);
                RenderLib.drawCenteredString(g, "ARE YOU SURE YOU WANT TO RESET LEVEL?", 200, 70, 15, "Comic Sans MS", 0, new Color(255,255,255));

                RenderLib.drawRect(40,130,170,40, new Color(115, 255, 0), g);
                RenderLib.drawRect(220,130,170,40, new Color(255, 0, 0), g);
                RenderLib.drawString(g, "press \"ESCAPE\" to not", 45, 150, 15, "Comic Sans MS", 0, new Color(0,0,0));
                RenderLib.drawString(g, "press \"R\" again to reset", 225, 150, 15, "Comic Sans MS", 0, new Color(0,0,0));

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

        mouseclicked = leftclicked;
        rightclicker = rightclicked;
    }

    public static int colorTextBox(int startnum) {
        // CODED AT 3AM I WILL MAKE IT NOT LOOK LIKE THIS LATER IDFC
        if (KeyLib.keyPressed(KeyEvent.VK_0)) startnum = Math.min(255, Integer.parseInt(startnum+"0"));
        else if (KeyLib.keyPressed(KeyEvent.VK_1)) startnum = Math.min(255, Integer.parseInt(startnum+"1"));
        else if (KeyLib.keyPressed(KeyEvent.VK_2)) startnum = Math.min(255, Integer.parseInt(startnum+"2"));
        else if (KeyLib.keyPressed(KeyEvent.VK_3)) startnum = Math.min(255, Integer.parseInt(startnum+"3"));
        else if (KeyLib.keyPressed(KeyEvent.VK_4)) startnum = Math.min(255, Integer.parseInt(startnum+"4"));
        else if (KeyLib.keyPressed(KeyEvent.VK_5)) startnum = Math.min(255, Integer.parseInt(startnum+"5"));
        else if (KeyLib.keyPressed(KeyEvent.VK_6)) startnum = Math.min(255, Integer.parseInt(startnum+"6"));
        else if (KeyLib.keyPressed(KeyEvent.VK_7)) startnum = Math.min(255, Integer.parseInt(startnum+"7"));
        else if (KeyLib.keyPressed(KeyEvent.VK_8)) startnum = Math.min(255, Integer.parseInt(startnum+"8"));
        else if (KeyLib.keyPressed(KeyEvent.VK_9)) startnum = Math.min(255, Integer.parseInt(startnum+"9"));
        else if (KeyLib.keyPressed(KeyEvent.VK_BACK_SPACE)) startnum = String.valueOf(startnum).length() == 1 ? 0 : Math.max(0, Integer.parseInt(String.valueOf(startnum).substring(0, String.valueOf(startnum).length()-1)));

        return startnum;
    };

    public static int onenumreplace(int num, int min, int max) {
        // CODED AT 3AM I WILL MAKE IT NOT LOOK LIKE THIS LATER IDFC

        if (KeyLib.keyPressed(KeyEvent.VK_0)) num = 0;
        else if (KeyLib.keyPressed(KeyEvent.VK_1)) num = 1;
        else if (KeyLib.keyPressed(KeyEvent.VK_2)) num = 2;
        else if (KeyLib.keyPressed(KeyEvent.VK_3)) num = 3;
        else if (KeyLib.keyPressed(KeyEvent.VK_4)) num = 4;
        else if (KeyLib.keyPressed(KeyEvent.VK_5)) num = 5;
        else if (KeyLib.keyPressed(KeyEvent.VK_6)) num = 6;
        else if (KeyLib.keyPressed(KeyEvent.VK_7)) num = 7;
        else if (KeyLib.keyPressed(KeyEvent.VK_8)) num = 8;
        else if (KeyLib.keyPressed(KeyEvent.VK_9)) num = 9;
        if (num < min) num = 10;
        num = Math.min(max, num);
        return num;
    };

}

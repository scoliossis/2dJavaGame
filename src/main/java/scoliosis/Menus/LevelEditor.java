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
import java.util.Iterator;

import static scoliosis.Display.mainframe;
import static scoliosis.Game.levelreader;
import static scoliosis.GameLibs.Velocity.xcoordinate;
import static scoliosis.GameLibs.Velocity.ycoordinate;
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


    public static void LevelEditor(BufferedImage bi, BufferStrategy bs) throws IOException {
        if (bs != null) {

            if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
                ScreenLib.changeScreen("title");
                Files.write(Paths.get(resourcesFile + "/levelcreator.cfg"), Locations.toString().getBytes(StandardCharsets.UTF_8));

                levelreader = "0,0,0,250,-16777216,0," + Files.readAllLines(Paths.get(resourcesFile + "/levelcreator.cfg")).toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");
                Game.levelreaderSplit = levelreader.split(",");
            }

            if (KeyLib.keyPressed(KeyEvent.VK_R)) {
                Locations.clear();
            }

            Graphics g = bs.getDrawGraphics();
            BufferedImage image = ImageIO.read(new File(resourcesFile + "/background.png"));
            g.drawImage(image, 0, 0, ScreenLib.width, ScreenLib.height, null);

            for (int i = xoffset%boxsize; i < 480; i+=boxsize) {
                RenderLib.drawLine(i, 0, i, 270, new Color(32,32,32), g);
            }
            for (int i = 0; i < 270; i+=boxsize) {
                RenderLib.drawLine(0, i, 480, i, new Color(32,32,32), g);
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

            if ((leftclicked || rightclicked) && (realmouseycoord() >= 35 || !showtopbar)) {
                boolean dontadd = false;
                for (int i = 0; i < Locations.size(); i += 6) {
                    if ((int) Locations.get(i) == mousexcoord(boxsize) * boxsize-xoffset) {

                        if ((int) Locations.get(i + 1) == mouseycoord(boxsize) * boxsize) {
                            dontadd = true;
                            toremove = i;
                        }
                    }
                }

                if (!dontadd && leftclicked) {
                    Locations.add(mousexcoord(boxsize) * boxsize-xoffset);
                    Locations.add(mouseycoord(boxsize) * boxsize);
                    Locations.add(1 * boxsize);
                    Locations.add(1 * boxsize);

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

            RenderLib.drawCircle(50+xoffset, 175, 25, 25, new Color(160, 250, 239), g);

            for (int i = 0; i < Locations.size(); i+=6) {
                RenderLib.drawRect((int) Locations.get(i)+xoffset, (int) Locations.get(i+1), (Integer) Locations.get(i+2), (Integer) Locations.get(i+3), new Color((Integer) Locations.get(i+4)), g);
                //RenderLib.drawOutline((int) Locations.get(i) * 10, (int) Locations.get(i+1) * 10, 10, 10, new Color(255,255,255), g);
            }

            if (showtopbar) {
                RenderLib.drawRect(0, 0, 480, 35, new Color(255, 255, 255), g);
                RenderLib.drawLine(0, 35, 480, 35, new Color(95, 131, 224), g);

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
                RenderLib.drawOutline(70, 20, 10, 10, new Color(48, 51, 63), g);


                RenderLib.drawRect(170, 2, 30, 14, new Color(215, 216, 229), g);
                RenderLib.drawOutline(170, 2, 30, 14, new Color(48, 51, 63), g);
                RenderLib.drawString(g, "S: " + boxsize, 172, 14, 12, "Comic Sans MS", 0, new Color(0, 0, 0));

                if (isMouseOverCoords(170, 2, 30, 14)) {
                    boxsize = onenumreplace(boxsize, 2, 10);
                }

                RenderLib.drawRect(170, 18, 30, 14, new Color(215, 216, 229), g);
                RenderLib.drawOutline(170, 18, 30, 14, new Color(48, 51, 63), g);
                RenderLib.drawString(g, "T: " + blocktype, 172, 30, 12, "Comic Sans MS", 0, new Color(0, 0, 0));

                if (isMouseOverCoords(170, 18, 30, 14)) {
                    blocktype = onenumreplace(blocktype, 0, 2);
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

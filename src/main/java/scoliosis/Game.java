package scoliosis;

import scoliosis.GameLibs.MoveLib;
import scoliosis.Libs.KeyLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Libs.RenderLib;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static scoliosis.GameLibs.MoveLib.onGround;
import static scoliosis.GameLibs.Velocity.*;
import static scoliosis.Libs.ScreenLib.height;
import static scoliosis.Main.resourcesFile;


public class Game {

    // dirty brown
    //150, 60, 30
    // grassy green
    // 58, 100, 21

    public static String levelreader = "";
    public static String[] levelreaderSplit = "".split(",");

    /*
    public static String[] textboxes = new String[]{
            "space", "shift + w"
    };
     */

    public static double fps = 0;
    static double timebeforetick = 0;
    static double timeaftertick = 0;


    public static int charecterheight = 25;
    public static int charecterwidth = 25;

    static int xtodraw = 0;
    static float fakex = 0;

    public static void game(BufferedImage bi, BufferStrategy bs) throws IOException {

        if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
            ScreenLib.changeScreen("pause");
        }


        MoveLib.MoveLibChecks();


        Color colorofball = new Color(105, 105, 252);
        Color outsidering = new Color(0, 0, 0);
        if (sprinting) {
            colorofball = new Color(18, 166, 255);
            outsidering = new Color(255, 255, 255);
        }


        if (xcoordinate < -30 || ycoordinate > 270 || KeyLib.keyPressed(KeyEvent.VK_R)) {
            xcoordinate = 50;
            ycoordinate = 200;
            xvelocity = 0;
            yvelocity = 0;
        }

        if (yvelocity < -2) {
            charecterheight = 20;
        }
        else if (yvelocity > 2) {
            charecterheight = 30;
        }
        else if (sprinting && !onGround) {
            charecterheight = 23;
        }

        else {
            charecterheight = 25;
        }

        timebeforetick = System.nanoTime();

        double millisbetweentick = timebeforetick - timeaftertick;

        timeaftertick = System.nanoTime();


        // so it doesnt check too often.
        if (System.currentTimeMillis() % 75 == 0)

            // gets the amount of seconds between each frame, then divide 1 by it.
            fps = 1 / (millisbetweentick / 1000000000);

        if (bs != null) {
            Graphics g = bs.getDrawGraphics();
            BufferedImage image = ImageIO.read(new File(resourcesFile + "/background.png"));
            g.drawImage(image, 0, 0, ScreenLib.width, ScreenLib.height, null);

            //g.drawImage(bi, 0, 0, ScreenLib.width, ScreenLib.height, null);

            if (xcoordinate != fakex) {
                if (xvelocity > 0) {
                    if (xcoordinate != 100) fakex += (xcoordinate - fakex) * 0.1f;
                    else fakex -= 0.1f;
                }
                else if (xvelocity < 0) {
                    if (xcoordinate != 200) fakex += (xcoordinate - fakex) * 0.1f;
                    else fakex += 0.1f;

                }
                else {
                    fakex += (xcoordinate - fakex) * 0.01f;
                }

            }

            int fakefakex = (int) fakex;
            int texnum = 0;

            for (int i = 0; i < levelreaderSplit.length; i+=6) {
                if (Integer.parseInt(levelreaderSplit[i+5]) != 0) {
                    xtodraw = Integer.parseInt(levelreaderSplit[i]) - fakefakex + 75;
                    RenderLib.drawRect(xtodraw, Integer.parseInt(levelreaderSplit[i+1]), Integer.parseInt(levelreaderSplit[i+2]), Integer.parseInt(levelreaderSplit[i+3]), new Color(Integer.parseInt(levelreaderSplit[i+4])), g);

                    if (Integer.parseInt(levelreaderSplit[i+5]) == 2) {
                        //RenderLib.drawCenteredString(g, textboxes[texnum], xtodraw + ((Integer.parseInt(levelreaderSplit[i+2]))/2), Integer.parseInt(levelreaderSplit[i+1])+((Integer.parseInt(levelreaderSplit[i+3]))/2), 15, "Comic Sans MS", 0, new Color(255, 255, 255));
                        texnum++;
                    }
                }
            }

            RenderLib.drawCircle(75 + (xcoordinate - fakefakex), ycoordinate-25, charecterwidth, charecterheight, colorofball, g);
            RenderLib.drawCircleOutline(75 + (xcoordinate - fakefakex), ycoordinate-25, charecterwidth, charecterheight, outsidering, g);

            RenderLib.drawString(g, "fps: " + Math.round(fps), 380, 30, 10, "Comic Sans MS", 0, new Color(0,0,0));

            for (int i = 0; i < levelreaderSplit.length; i+=6) {
                if (Integer.parseInt(levelreaderSplit[i+5]) == 0) {
                    xtodraw = Integer.parseInt(levelreaderSplit[i]) - fakefakex + 75;
                    RenderLib.drawRect(xtodraw, Integer.parseInt(levelreaderSplit[i+1]), Integer.parseInt(levelreaderSplit[i+2]), Integer.parseInt(levelreaderSplit[i+3]), new Color(Integer.parseInt(levelreaderSplit[i+4])), g);
                }
            }

            g.dispose();
            bs.show();
        }
    }
}

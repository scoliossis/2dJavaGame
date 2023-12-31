package scoliosis.Menus;

import javafx.scene.Scene;
import scoliosis.Display;
import scoliosis.Libs.KeyLib;
import scoliosis.Libs.MouseLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Options.Config;
import scoliosis.Options.Configs;
import scoliosis.Options.Setting;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static scoliosis.Display.mainframe;
import static scoliosis.Main.resourcesFile;

public class OptionsScreen {

    static int i = 0;
    static int iadd = 26;

    static Boolean leftclicked = false;

    public static ArrayList settings = Config.collect(Configs.class);
    public static void optionsMenu(BufferedImage bi, BufferStrategy bs) throws IOException {
        if (bs != null) {

            if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
                ScreenLib.changeScreen("title");

                Config.save();
            }

            Graphics g = bs.getDrawGraphics();

            Iterator allsettings = OptionsScreen.settings.iterator();

            BufferedImage image = ImageIO.read(new File(resourcesFile + "/titlescreen.png"));
            g.drawImage(image, 0, 0, ScreenLib.width, ScreenLib.height, null);
            //RenderLib.drawRect(5, 5, 450, 210, new Color(25, 25, 25), g);

            i = 0;
            while (allsettings.hasNext()) {
                Setting setting = (Setting) allsettings.next();

                int defx = 0;

                if (i/iadd % 2 == 1) {
                    defx = 235;
                }
                else {
                    i += iadd;
                    defx = 10;
                }

                if (MouseLib.isMouseOverCoords(defx, i, 200, 50)) {

                    RenderLib.drawRect(0,0, 480, 20, new Color(22,22,22,22), g);
                    RenderLib.drawString(g, setting.description, 5, 13, 8, "Comic Sans MS", 0, new Color(255,255,255));

                    if (leftclicked != MouseLib.leftclicked && MouseLib.leftclicked) {
                        leftclicked = MouseLib.leftclicked;
                        setting.set(!(Boolean) setting.get(Boolean.class));
                    }
                }


                RenderLib.drawRect(defx, i, 200, 50, new Color(226, 99, 16), g);

                if ((Boolean) setting.get(Boolean.class)) {
                    RenderLib.drawRect(defx + 200 - 60, i + 25, 50, 20, new Color(0, 255, 0), g);
                    RenderLib.drawCircle(defx + 200 - 30, i + 25, 19, 19, new Color(255, 255, 255), g);
                }
                else {
                    RenderLib.drawRect(defx + 200 - 60, i + 25, 50, 20, new Color(255, 0, 0), g);
                    RenderLib.drawCircle(defx + 200 - 60, i + 25, 19, 19, new Color(255, 255, 255), g);
                }

                if (i/iadd % 2 == 1 && defx != 10) {
                    i += iadd;
                }
            }


            //g.drawImage(bi, 0, 0, ScreenLib.width, ScreenLib.height, null);


            allsettings = OptionsScreen.settings.iterator();

            i = 0;
            while (allsettings.hasNext()) {
                Setting setting = (Setting) allsettings.next();

                if (i/iadd % 2 == 1) {
                    RenderLib.drawString(g, setting.name, 240, i + 20, 20, "SansSerif", Font.PLAIN, new Color(255, 255, 255));
                    i += iadd;
                }
                else {
                    i += iadd;
                    RenderLib.drawString(g, setting.name, 15, i + 20, 20, "SansSerif", Font.PLAIN, new Color(255, 255, 255));
                }
            }

            g.dispose();
            bs.show();
        }

        if (!MouseLib.leftclicked) {
            leftclicked = false;
        }
    }
}

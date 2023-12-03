package scoliosis.Menus;

import scoliosis.Libs.KeyLib;
import scoliosis.Libs.MouseLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Options.Config;
import scoliosis.Options.Setting;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class PauseMenu {
    public static void PauseMenu(BufferedImage bi, BufferStrategy bs) {

        if (KeyLib.keyPressed(KeyEvent.VK_ESCAPE)) {
            ScreenLib.changeScreen("game");
        }

        if (bs != null) {

            Graphics g = bs.getDrawGraphics();

            RenderLib.drawRect(15, 10, 10, 35, new Color(255, 255, 255), g);
            RenderLib.drawRect(30, 10, 10, 35, new Color(255, 255, 255), g);

            RenderLib.drawCircle(180, 110, 50, 50, new Color(127, 162, 255), g);
            RenderLib.drawCircle(190, 115, 10, 10, new Color(0, 0, 0), g);
            RenderLib.drawCircle(190, 130, 10, 10, new Color(0, 0, 0), g);
            RenderLib.drawCircle(190, 145, 10, 10, new Color(0, 0, 0), g);
            RenderLib.drawRect(202, 117, 17, 8, new Color(0, 0, 0), g);
            RenderLib.drawRect(202, 132, 17, 8,  new Color(0, 0, 0), g);
            RenderLib.drawRect(202, 147, 17, 8, new Color(0, 0, 0), g);

            if (MouseLib.isMouseOverCoords(175, 105, 50, 50) && MouseLib.leftclicked) {
                ScreenLib.changeScreen("title");
            }

            g.dispose();
            bs.show();
        }
    }
}

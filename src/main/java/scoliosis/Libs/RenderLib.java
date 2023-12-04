package scoliosis.Libs;

import scoliosis.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static scoliosis.Display.*;
import static scoliosis.Main.resourcesFile;

public class RenderLib {


    public static void drawRect(int x, int y, int width, int height, Color color) {

        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        height =  (int) (height/270f *(float) (mainframe.getHeight()));
        width = (int) (width/480f *(float) (mainframe.getWidth()));
        for (int xc = x; xc < x+width; xc++) {
            for (int yc = y; yc < y+height; yc++) {
                try {
                    if (xc >= x && xc <= x + width && yc >= y && yc <= y+height && yc < mainframe.getHeight() && xc < mainframe.getWidth()) {

                        //int anim = (int) (Math.sin((double) (System.currentTimeMillis() % 1000) / 1000 * Math.PI * 2) * 50);
                        //int anim2 = (int) (Math.cos((double) (System.currentTimeMillis() % 1000) / 1000 * Math.PI * 2) * 50);
                        Display.bi.setRGB(xc, yc, color.getRGB());
                    }
                } catch (Exception ignored) {
                    // idk why but this makes it no good
                    //System.out.println("tab resized!");
                }
            }
        }
    }

    public static void drawRect(int x, int y, int width, int height, Color color, Graphics g) {
        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        height =  (int) (height/270f *(float) (mainframe.getHeight()));
        width = (int) (width/480f *(float) (mainframe.getWidth()));

        g.setColor(color);
        //g.drawRect(x, y, width, height);
        g.fillPolygon(new int[] {x, x+width, x+width, x}, new int[] {y, y, y+height, y+height}, 4);
    }


    public static void drawOutline(int x, int y, int width, int height, Color color) {

        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        height =  (int) (height/270f *(float) (mainframe.getHeight()));
        width = (int) (width/480f *(float) (mainframe.getWidth()));

        for (int yc = y; yc < y + height; yc++) {
            if (yc < mainframe.getHeight() && x < mainframe.getWidth()) {
                Display.bi.setRGB(x, yc, color.getRGB());
                Display.bi.setRGB(x + width, yc, color.getRGB());
            }
        }

        for (int xc = x; xc < x + width; xc++) {
            if (y < mainframe.getHeight() && xc < mainframe.getWidth()) {
                Display.bi.setRGB(xc, y, color.getRGB());
                Display.bi.setRGB(xc, y + height, color.getRGB());
            }
        }
    }

    public static void drawOutline(int x, int y, int width, int height, Color color, Graphics g) {
        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        height =  (int) (height/270f *(float) (mainframe.getHeight()));
        width = (int) (width/480f *(float) (mainframe.getWidth()));

        g.setColor(color);
        g.drawRect(x, y, width, height);
    }


    public static void drawCircle(int x, int y, int radius, Color color) {

        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        radius = (int) (radius/480f *(float) (mainframe.getWidth()));

        for (int i = 0; i < 360; i += 1) {
            for (int r = 0; r < radius/2; r++) {
                double angle = i * Math.PI / 180;

                int x2 = (int) (x + r * Math.cos(angle));
                int y2 = (int) (y + r * Math.sin(angle));

                if (x2 < ScreenLib.width && x2 > 0 && y2 < ScreenLib.height && y2 > 0) Display.bi.setRGB(x2, y2, color.getRGB());
            }
        }
    }

    public static void drawCircle(int x, int y, int width, int height, Color color, Graphics g) {
        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        width = (int) (width/480f *(float) (mainframe.getWidth()));
        height = (int) (height/270f *(float) (mainframe.getHeight()));

        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    public static void drawCircleOutline(int x, int y, int radius, Color color) {

        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        radius = (int) (radius/480f *(float) (mainframe.getWidth()));

        for (int i = 0; i < 360; i += 1) {
            double angle = i * Math.PI / 180;

            int x2 = (int) (x + radius/2 * Math.cos(angle));
            int y2 = (int) (y + radius/2 * Math.sin(angle));

            if (x2 < ScreenLib.width && x2 > 0 && y2 < ScreenLib.height && y2 > 0) Display.bi.setRGB(x2, y2, color.getRGB());
        }
    }

    public static void drawCircleOutline(int x, int y, int width, int height, Color color, Graphics g) {
        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        width = (int) (width/480f *(float) (mainframe.getWidth()));
        height = (int) (height/270f *(float) (mainframe.getHeight()));

        g.setColor(color);
        g.drawOval(x, y, width, height);
    }

    public static void drawCircleRealLocation(int x, int y, int radius, Color color) {

        radius = (int) (radius/480f *(float) (mainframe.getWidth()));

        for (int i = 0; i < 360; i += 1) {
            for (int r = 0; r < radius/2; r++) {
                double angle = i * Math.PI / 180;

                int x2 = (int) (x + r * Math.cos(angle));
                int y2 = (int) (y + r * Math.sin(angle));

                if (x2 < ScreenLib.width && x2 > 0 && y2 < ScreenLib.height && y2 > 0) Display.bi.setRGB(x2, y2, color.getRGB());
            }
        }
    }

    public static void drawCircleRealLocation(int x, int y, int width, int height, Color color, Graphics g) {
        width = (int) (width/480f *(float) (mainframe.getWidth()));
        height = (int) (height/270f *(float) (mainframe.getHeight()));

        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    public static void drawCircleOutlineRealLocation(int x, int y, int radius, Color color) {


        radius = (int) (radius/480f *(float) (mainframe.getWidth()));

        for (int i = 0; i < 360; i += 1) {
            double angle = i * Math.PI / 180;

            int x2 = (int) (x + radius/2 * Math.cos(angle));
            int y2 = (int) (y + radius/2 * Math.sin(angle));

            if (x2 < ScreenLib.width && x2 > 0 && y2 < ScreenLib.height && y2 > 0) Display.bi.setRGB(x2, y2, color.getRGB());
        }

    }

    public static void drawCircleOutlineRealLocation(int x, int y, int width, int height, Color color, Graphics g) {
        width = (int) (width/480f *(float) (mainframe.getWidth()));
        height = (int) (height/270f *(float) (mainframe.getHeight()));

        g.setColor(color);
        g.drawOval(x, y, width, height);
    }

    public static void drawString(Graphics graphics, String text, int x, int y, int fontsize, String fontname, int style, Color color) {
        Font font = new Font(fontname, style, (int) (fontsize/480f *(float) (mainframe.getWidth())));

        graphics.setFont(font);
        graphics.setColor(color);

        graphics.drawString(text, (int) (x/480f *(float) (mainframe.getWidth())), (int) (y/270f *(float) (mainframe.getHeight())));
    }

    public static void drawCenteredString(Graphics graphics, String text, int x, int y, int fontsize, String fontname, int style, Color color) {
        Font font = new Font(fontname, style, (int) (fontsize/480f *(float) (mainframe.getWidth())));
        x = (int) (x/480f *(float) (mainframe.getWidth()));

        graphics.setFont(font);
        graphics.setColor(color);
        x -= graphics.getFontMetrics().stringWidth(text)/2;

        graphics.drawString(text, x, (int) (y/270f *(float) (mainframe.getHeight())));
    }


    public static void drawLine(int x, int y, int x2, int y2, Color color, Graphics g) {
        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        x2 = (int) (x2/480f *(float) (mainframe.getWidth()));
        y2 =  (int) (y2/270f *(float) (mainframe.getHeight()));

        g.setColor(color);
        g.drawLine(x, y, x2, y2);
    }

    public static void drawPoligon(int[] x, int[] y, Color color, Graphics g) {

        g.setColor(color);

        for (int i = 0; i < x.length; i++) {
            x[i] = (int) (x[i]/480f *(float) (mainframe.getWidth()));
            y[i] = (int) (y[i]/270f *(float) (mainframe.getHeight()));
        }

        g.fillPolygon(x, y, x.length);

    }



    public static void drawImage(int x, int y, int width, int height, String path, Graphics g) {
        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        height =  (int) (height/270f *(float) (mainframe.getHeight())) + 1;
        width = (int) (width/480f *(float) (mainframe.getWidth())) + 1;
        try {
            BufferedImage image = ImageIO.read(new File(resourcesFile + "/" + path + ".png"));

            g.drawImage(image, x, y, width, height, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
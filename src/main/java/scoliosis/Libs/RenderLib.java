package scoliosis.Libs;

import javafx.scene.SubScene;
import scoliosis.Display;
import scoliosis.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static scoliosis.Display.*;
import static scoliosis.Main.*;

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

    public static int getStringWidth(String text, Font font, Graphics graphics) {

        graphics.setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize()/480f *(float) (mainframe.getWidth()))));

        return graphics.getFontMetrics().stringWidth(text);
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

    public static int numberOfImages = 0;
    public static BufferedImage[] allimages;
    public static String[] ImageNames;

    public static BufferedImage[] background = new BufferedImage[backgrounds.length];

    static BufferedImage[] currentimage = new BufferedImage[textures.length];


    public static void loadImages() {
        try {
            for (int i = 0; i < textures.length; i++) {
                currentimage[i] = ImageIO.read(new File(resourcesFile + "/"+textures[i]+".png"));
            }
            for (int i = 0; i < backgrounds.length; i++) {
                background[i] = ImageIO.read(new File(resourcesFile + "/" + backgrounds[i] + ".png"));
            }

            for (File file : Objects.requireNonNull(Paths.get(resourcesFile).toFile().listFiles())) {
                if (file.getName().endsWith(".png")) {
                    numberOfImages++;
                }
            }
            allimages = new BufferedImage[numberOfImages];
            ImageNames = new String[numberOfImages];
            int i = 0;
            for (File file : Objects.requireNonNull(Paths.get(resourcesFile).toFile().listFiles())) {
                if (file.getName().endsWith(".png")) {
                    allimages[i] = ImageIO.read(new File(resourcesFile + "/" + file.getName()));
                    ImageNames[i] = file.getName();
                    i++;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void drawImage(int x, int y, int width, int height, BufferedImage bi, Graphics g) {
        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        height =  (int) (height/270f *(float) (mainframe.getHeight())) + 1;
        width = (int) (width/480f *(float) (mainframe.getWidth())) + 1;

        g.drawImage(bi, x, y, width, height, null);
    }

    public static void drawImage(int x, int y, int width, int height, int imagenum, Graphics g) {
        x = (int) (x/480f *(float) (mainframe.getWidth()));
        y = (int) (y/270f *(float) (mainframe.getHeight()));

        height =  (int) (height/270f *(float) (mainframe.getHeight())) + 1;
        width = (int) (width/480f *(float) (mainframe.getWidth())) + 1;

        g.drawImage(currentimage[imagenum], x, y, width, height, null);
    }

    public static BufferedImage splitBufferedImage(int x, int y, int w, int h, BufferedImage image) {
        BufferedImage image2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int xc = 0; xc < w; xc++) {
            for (int yc = 0; yc < h; yc++) {
                image2.setRGB(xc,yc, image.getRGB(xc+x, yc+y));
            }
        }
        return image2;
    }

    public static BufferedImage getBufferedImage(String h) {
        BufferedImage image2 = allimages[0];

        if (Game.ticks % 100 >= 80 && h == "coin1") h="coin2";
        else if (h == "coin3" && Game.ticks % 100 >= 30 && Game.ticks % 100 <= 40) h = "coin4";

        for (int i = 0; i < numberOfImages; i++) {
            if (ImageNames[i].replace(".png", "").replace(" ", "").equalsIgnoreCase(h)) {
                image2 = allimages[i];
            }
        }

        return image2;
    }

    // horribly slow + an ehh looking blur
    public static BufferedImage BlurBufferedImage(BufferedImage image, int bluramoun) {
        BufferedImage blurred = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = -bluramoun; x < image.getWidth(); x+=bluramoun) {
            for (int y = -bluramoun; y < image.getHeight(); y+=bluramoun) {

                for (int i = 0; i < 360; i += 1) {
                    for (int r = 0; r < bluramoun; r++) {
                        double angle = i * Math.PI / 180;

                        int x2 = (int) (x + r * Math.cos(angle));
                        int y2 = (int) (y + r * Math.sin(angle));

                        if (x2 < ScreenLib.width && x2 > 0 && y2 < ScreenLib.height && y2 > 0 && x < ScreenLib.width && x > 0 && y < ScreenLib.height && y > 0) {
                            blurred.setRGB(x2, y2, image.getRGB(x, y));
                        }
                    }
                }
            }
        }

        return blurred;
    }


    public static BufferedImage BlurBufferedImage(BufferedImage image) {
        int radius = 11;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        Arrays.fill(data, weight);

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp filter = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        BufferedImage BlurredImage = filter.filter(image, null);


        return BlurredImage.getSubimage(12, 12, image.getWidth()-12-(image.getWidth()/10), image.getHeight()-12-(image.getHeight()/10));
    }
}
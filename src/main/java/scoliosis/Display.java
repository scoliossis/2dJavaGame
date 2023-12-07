package scoliosis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import java.awt.Canvas;

import scoliosis.Libs.KeyLib;
import scoliosis.Libs.ScreenLib;
import scoliosis.Menus.*;

import static scoliosis.Libs.ScreenLib.screenSize;

public class Display extends Canvas {

    public static BufferedImage bi;
    public static int[] pixels;

    public Display() {
        bi = new BufferedImage(ScreenLib.width, ScreenLib.height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
    }

    public static boolean titlescreen = true;
    public static boolean optionsmenu = false;
    public static boolean ingame = false;
    public static boolean pausescreen = false;
    public static boolean leveleditor = false;
    public static boolean chooselevel = false;

    public void startGame() {

        while (true) {

            try {

                ScreenLib.width = mainframe.getWidth();
                ScreenLib.height = mainframe.getHeight();

                bi = new BufferedImage(ScreenLib.width, ScreenLib.height, BufferedImage.TYPE_INT_RGB);

                BufferStrategy bs = this.getBufferStrategy();

                if (bs == null) createBufferStrategy(3);

                if (ingame) Game.game(bi, bs);
                else if (pausescreen) PauseMenu.PauseMenu(bi, bs);
                else if (titlescreen) TitleScreen.titleScreen(bi, bs);
                else if (optionsmenu) OptionsScreen.optionsMenu(bi, bs);
                else if (leveleditor) LevelEditor.LevelEditor(bi, bs);
                else if (chooselevel) ChooseLevel.LevelSelector(bi, bs);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }



    public static JFrame mainframe = new JFrame();

    public static void DrawDisplay() {
        Display game = new Display();

        mainframe = new JFrame("scoliosis on top!");
        mainframe.add(game);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize((int) screenSize.getWidth() / 4, (int) screenSize.getHeight() / 4);
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);

        mainframe.addKeyListener(new KeyLib());
        Toolkit.getDefaultToolkit().beep();

        game.startGame();
    }
}
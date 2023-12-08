package scoliosis;

import scoliosis.Libs.MouseLib;
import scoliosis.Libs.SoundLib;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    /*
    todo: as of 03/12/23 13:00
    ---- level editor ----
    color picker - added to list: 3/12/23 - DONE 03/12/23 14:32
    show previous shit from practise - added to list: 3/12/23 - DONE 03/12/23 15:01
    make gui to choose level - added to list: 3/12/23 - DONE 07/12/23 22:05
    change spawn point - added to list: 3/12/23
    different blocks (lava, finish flag) - added to list: 3/12/23 - DONE 03/12/23 19:50
    custom block textures - added to list: 3/12/23 - DONE 04/12/23 20:32
    change background option - added to list: 3/12/23

    ---- gameplay ----
    make campaign - added to list: 3/12/23 - Started:
        level 1-1 - 03/12/23 19:41
        level 8-1 - 05/12/23 8:20

    make hard mode / hardcore - added to list: 7/12/23
    make practise for campaign levels - added to list: 7/12/23
    make special tab for custom levels - added to list: 7/12/23
    add timer - added to list: 3/12/23 - DONE 03/12/23 19:50
    add enemies with set paths - added to list: 3/12/23
    add music - added to list: 3/12/23
    win screen - added to list: 3/12/23 - DONE 03/12/23 21:19
    death screen / animation - added to list: 3/12/23 - DONE 05/12/23 20:33
    levels screen - added to list: 3/12/23
    store multiple levels - added to list: 3/12/23 - DONE 03/12/23 21:16

    ---- bug fixes ----
    fix collision issues - added 3/12/23 - is a feature for now :sunglasses:
    moving while paused - added to list 3/12/23 - DONE 04/12/23 21:18
    no collisions if fps drop - added to list 3/12/23 - DONE 04/12/23 21:18
    move WAY too fast if fps drop - added to list 3/12/23 - DONE 05/12/23 17:06
    no collisions in negative coordinates - added to list 05/12/23 - may not fix as is slightly useless...
    textures in level editor going outside box boundaries - added to list 05/12/23 - DONE 05/12/23 21:18
    placing textures behind top bar while top bar still visible - added to list 05/12/23 - DONE 07/12/23 0:15
     */

    public static void main(String[] args) throws IOException {
        Toolkit.getDefaultToolkit().addAWTEventListener(new MouseLib(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
        loadresources();
        Game.loadMap(maptoload);

        Display.DrawDisplay();
    }

    public static String maptoload = "1-1.map";
    public static String[] backgrounds = {"background", "mushroombackground", "cavebackground", "desertbackground", "skybackground", "swampbackground", "rainbackground", "lavabackground"};

    public static String[] textures = {"dirt", "grass", "sand", "poison", "lavabrick", "coal", "lava", "smoke", "flagpole", "base", "circle", "triangle", "square", "brick", "bluebrick", "whitebrick", "purplebrick", "redbrick", "brownbrick"};


    public static String scoliosis = System.getenv("APPDATA") + "\\scoliosis";
    public static String baseName = System.getenv("APPDATA") + "\\scoliosis\\2dBallGame";
    public static String resourcesFile = baseName + "\\resources";

    public static void loadresources() {

        if (!Files.isDirectory(Paths.get(scoliosis))) new File(scoliosis).mkdir();
        if (!Files.isDirectory(Paths.get(baseName))) new File(baseName).mkdir();
        if (!Files.isDirectory(Paths.get(resourcesFile))) new File(resourcesFile).mkdir();


        // copy files outside of resources file
        try {

            copyFileOut("files.txt");


            String[] files = Files.readAllLines(Paths.get(resourcesFile + "\\files.txt")).toString().replace("]", "").split(",");
            for (String file : files) {
                copyFileOut(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // check if resources directory is there (it always is but just in case)
        if (Files.isDirectory(Paths.get(resourcesFile))) {

            // get time at start or loading shit
            double starttime = System.currentTimeMillis();

            // for every file in the resources folder
            for (File file : Objects.requireNonNull(Paths.get(resourcesFile).toFile().listFiles())) {

                // .wav loader is very nice!
                // goes from taking 323ms to start playing file to 3ms on first play!
                // actually big dif no way!!!
                if (file.toString().endsWith(".wav")) {
                    try{
                        SoundLib.playSound(file.getName());
                        System.out.println("loaded " + file.getName());

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                // png loader is sorta useless, saves ~ 10 milliseconds on the first draw of each image
                // when i tested on a 95.5KB image it went from 41 millis to 31 millis to do first draw
                else if (file.toString().endsWith(".png")) {
                    try {

                        // opens file once, so it will open faster next time (barely saves any time)
                        ImageIO.read(file);
                        System.out.println("loaded " + file.getName());
                    }
                    catch (IOException e) {

                    }
                }
            }
            System.out.println("time took to load resources: " + (System.currentTimeMillis()-starttime)/1000 + " seconds");
        }
        else {
            System.out.println("cant find resources file??????");
        }
    }

    public static void copyFileOut(String filename) throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        if (filename == "files.txt") Files.delete(Paths.get(System.getenv("APPDATA") + "\\scoliosis\\2dBallGame\\resources\\" + filename));

        if (!Files.exists(Paths.get(System.getenv("APPDATA") + "\\scoliosis\\2dBallGame\\resources\\"+filename)))
        if (inputStream != null) Files.copy(inputStream, Paths.get(System.getenv("APPDATA") + "\\scoliosis\\2dBallGame\\resources\\"+filename));
    }

}

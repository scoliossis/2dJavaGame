package scoliosis;

import scoliosis.Libs.MouseLib;
import scoliosis.Libs.RenderLib;
import scoliosis.Libs.SoundLib;
import scoliosis.Menus.ChooseLevel;
import scoliosis.Options.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    /*
    todo: as of 03/12/23 13:00
    DONE:

    ---- level editor ----
     ̶c̶o̶l̶o̶r̶ ̶p̶i̶c̶k̶e̶r̶ ̶-̶ ̶a̶d̶d̶e̶d̶ ̶t̶o̶ ̶l̶i̶s̶t̶:̶ ̶3̶/̶1̶2̶/̶2̶3̶ ̶-̶ ̶D̶O̶N̶E̶ ̶0̶3̶/̶1̶2̶/̶2̶3̶ ̶1̶4̶:̶3̶2̶ - removed - 04/12/23 20:32
    show previous shit from practise - added to list: 3/12/23 - DONE 03/12/23 15:01
    make gui to choose level - added to list: 3/12/23 - DONE 07/12/23 22:05
    different blocks (lava, finish flag) - added to list: 3/12/23 - DONE 03/12/23 19:50
    custom block textures - added to list: 3/12/23 - DONE 04/12/23 20:32
    draw line / box tool - added to list: 10/12/23 - DONE 10/12/23 14:58

    ---- gameplay ----
    make practise for campaign levels - added to list: 7/12/23 - DONE 09/12/23 17:02
    add timer - added to list: 3/12/23 - DONE 03/12/23 19:50
    win screen - added to list: 3/12/23 - DONE 03/12/23 21:19
    death screen / animation - added to list: 3/12/23 - DONE 05/12/23 20:33
    levels screen - added to list: 3/12/23 - DONE 09/12/23 17:02
    store multiple levels - added to list: 3/12/23 - DONE 03/12/23 21:16
    coins!!! - added to list: 28/12/23 - DONE 28/12/23 ~20:00

    ---- bug fixes ----
     ̶n̶o̶ ̶c̶o̶l̶l̶i̶s̶i̶o̶n̶s̶ ̶i̶n̶ ̶n̶e̶g̶a̶t̶i̶v̶e̶ ̶c̶o̶o̶r̶d̶i̶n̶a̶t̶e̶s̶ ̶-̶ ̶a̶d̶d̶e̶d̶ ̶t̶o̶ ̶l̶i̶s̶t̶ ̶0̶5̶/̶1̶2̶/̶2̶3̶ - idrc, its not needed for now
     ̶f̶i̶x̶ ̶c̶o̶l̶l̶i̶s̶i̶o̶n̶ ̶i̶s̶s̶u̶e̶s̶ ̶-̶ ̶a̶d̶d̶e̶d̶ ̶t̶o̶ ̶l̶i̶s̶t̶ ̶0̶̶3̶̶/̶1̶2̶/̶2̶3̶ - is a feature for now :sunglasses:
    moving while paused - added to list 3/12/23 - DONE 04/12/23 21:18
    no collisions if fps drop - added to list 3/12/23 - DONE 04/12/23 21:18
    move WAY too fast if fps drop - added to list 3/12/23 - DONE 05/12/23 17:06
    textures in level editor going outside box boundaries - added to list 05/12/23 - DONE 05/12/23 21:18
    placing textures behind top bar while top bar still visible - added to list 05/12/23 - DONE 07/12/23 0:15
    fix low fps being UNPLAYABLE - added to list 10/12/23 - DONE 14:59


    NOT DONE!
    NOT DONE!
    NOT DONE!

    ---- level editor ----
    change spawn point - added to list: 3/12/23
    change background option - added to list: 3/12/23

    ---- gameplay ----
    make campaign - added to list: 3/12/23 - Started:
        level 1-1 - 03/12/23 19:41
        level 1-2 - 09/12/23 17:01
        level 1-3 - ~20/12/23
        level 1-4 - ~21/12/23

        level 2-1 - 29/12/23
        level 2-2 - 29/12/23
        level 2-3 - 29/12/23

        level 8-1 - 05/12/23 8:20

    add enemies with set paths - added to list: 3/12/23
    add music - added to list: 3/12/23
    make special tab for custom levels - added to list: 7/12/23
    make hard mode / hardcore - added to list: 7/12/23
    make no jump / no sprint mode! - added to list: 30/12/23

    ---- bug fixes ----
    fps issues ig - added to list 09/12/23

    ==== other ====
    put shit into functions so code is readable - added to list 10/12/23
     */

    public static void main(String[] args) throws IOException {
        load();

        Display.DrawDisplay();
    }

    public static String[] backgrounds = {"background", "mushroombackground", "cavebackground", "desertbackground", "skybackground", "swampbackground", "rainbackground", "lavabackground"};

    public static String[] textures = {"dirt", "grass", "sand", "poison", "lavabrick", "coal", "lava", "smoke", "flagpole", "base", "circle", "triangle", "square", "brick", "bluebrick", "whitebrick", "purplebrick", "redbrick", "brownbrick", "stone", "darkstone", "water", "fungaldirt", "fungalgrass", "fungalwater", "mossybrick", "coin1"};

    public static String[] challenges = {"noSprint", "noJump", "hard", "hardcore", "collector"};


    public static String scoliosis = System.getenv("APPDATA") + "\\scoliosis";
    public static String baseName = System.getenv("APPDATA") + "\\scoliosis\\2dBallGame";
    public static String resourcesFile = baseName + "\\resources";

    public static String[] completedLevels;
    public static int highestUnlockedWorld = 0;
    public static int highestUnlockedLevel = 0;

    public static void load() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new MouseLib(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);

        loadresources();
        RenderLib.loadImages();

        ChooseLevel.getLevels();
        getCompletedLevels();
        Config.load();
    }

    public static void getCompletedLevels() {
        if (Files.exists(Paths.get(baseName + "/times.scolio"))) {
            try {
                String fileInfo = Files.readAllLines(Paths.get(baseName + "/times.scolio")).toString();
                String[] fileSplit = fileInfo.replaceAll("\\[", "").replaceAll("]", "").split(",");
                completedLevels = new String[fileSplit.length];
                for (int i = 0; i < fileSplit.length; i++) {
                    completedLevels[i] = fileSplit[i].replace(".map", "").split(":")[0];
                    int worldNumber = Integer.parseInt(completedLevels[i].split("-")[0]);
                    int levelNumber = Integer.parseInt(completedLevels[i].split("-")[1]);

                    if (worldNumber > highestUnlockedWorld) {
                        highestUnlockedWorld = worldNumber;
                        highestUnlockedLevel = levelNumber;
                    } else if (worldNumber == highestUnlockedWorld) {
                        if (levelNumber > highestUnlockedLevel) highestUnlockedLevel = levelNumber;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void makeResourcesFolder() {
        if (!Files.isDirectory(Paths.get(scoliosis))) new File(scoliosis).mkdir();
        if (!Files.isDirectory(Paths.get(baseName))) new File(baseName).mkdir();
        if (!Files.isDirectory(Paths.get(resourcesFile))) new File(resourcesFile).mkdir();
    }

    public static void loadresources() {

        makeResourcesFolder();


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
                        SoundLib.loadSound(file.getName());
                        //System.out.println("loaded " + file.getName());

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
                        //System.out.println("loaded " + file.getName());
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

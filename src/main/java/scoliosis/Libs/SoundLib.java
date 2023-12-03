package scoliosis.Libs;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import static scoliosis.Main.resourcesFile;

public class SoundLib {
    public static void playSound(String soundname){
        double timeatstart = System.currentTimeMillis();

        File lol = new File(resourcesFile + "/" + soundname);
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(lol));
            clip.start();
            //System.out.println("time took to open and start sound file: " + (System.currentTimeMillis() - timeatstart));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

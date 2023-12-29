package scoliosis.Libs;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;

import static scoliosis.Main.resourcesFile;

public class SoundLib {

    public static void playSound(String soundname){
        try{
            File lol = new File(resourcesFile + "/" + soundname);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(lol));
            clip.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void loadSound(String soundname){

        try{
            File lol = new File(resourcesFile + "/" + soundname);

            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(lol));
            clip.start();
            clip.stop();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

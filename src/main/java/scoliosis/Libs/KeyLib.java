package scoliosis.Libs;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class KeyLib extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    static ArrayList<Integer> keysdown = new ArrayList<>();
    static ArrayList<Integer> newKeys = new ArrayList<>();


    @Override
    public void keyPressed(KeyEvent e) {
        if (!keysdown.contains(e.getKeyCode()) && !newKeys.contains(e.getKeyCode())) keysdown.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysdown.remove((Integer) e.getKeyCode());
        newKeys.remove((Integer) e.getKeyCode());
    }

    public static boolean isKeyDown(int keycode) {
        for (int key : keysdown) {
            if (key == keycode) {
                return true;
            }
        }
        return false;
    }

    public static boolean keyPressed(int keycode) {
        for (int key : keysdown) {
            if (key == keycode) {
                if (!newKeys.contains(keycode)) {
                    newKeys.add(keycode);
                    return true;
                }
            }
        }
        return false;
    }
}
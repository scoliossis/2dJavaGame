package scoliosis.Options.Settings;


import scoliosis.Options.Property;
import scoliosis.Options.Setting;

import java.lang.reflect.Field;

public class SliderSetting extends Setting {

    public int step;

    public String suffix;

    public String prefix;

    public int min;

    public int max;

    public SliderSetting(Property var1, Field var2) {
        super(var1, var2);
        this.step = var1.step();
        this.min = var1.min();
        this.max = var1.max();
    }

    public boolean set(Object var1) {
        Float numret = Math.max((Float) var1, 0);
        numret = Math.min(numret, 255);
        return super.set(numret);
    }

    public int compareTo(Float var1) {
        try {
            return Float.compare((Float) this.get(Float.TYPE), var1);
        } catch (Exception var3) {
            return 0;
        }
    }
}

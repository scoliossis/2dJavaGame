package scoliosis.Options;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class Setting {

    public Setting parent = null;

    public Property annotation;

    public int height = 0;

    public Field field;

    public ArrayList sons = new ArrayList();

    public String name;
    public String description;


    public Setting(Property var1, Field var2) {
        this.annotation = var1;
        this.field = var2;
        this.name = var1.name();
        this.description = var1.description();
    }

    public int hashCode() {
        return this.name.hashCode();
    }


    public boolean equals(Object var1) {
        return var1 instanceof Setting && ((Setting) var1).name.equals(this.name);
    }


    public boolean set(Object var1) {
        try {
            this.field.set(var1.getClass(), var1);
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public ArrayList getSons() {
        ArrayList var1 = new ArrayList();
        Iterator var2 = this.sons.iterator();

        Setting var3;
        while (var2.hasNext()) {
            var3 = (Setting) var2.next();
            if (var3.sons.size() == 0) {
                var1.add(var3);
            }
        }

        var2 = this.sons.iterator();

        while (var2.hasNext()) {
            var3 = (Setting) var2.next();
            if (var3.sons.size() != 0) {
                var1.add(var3);
                var1.addAll(var3.getSons());
            }
        }

        return var1;
    }

    public Object get(Class var1) {
        try {
            return var1.cast(this.field.get(Object.class));
        } catch (Exception var3) {
            return null;
        }
    }
}

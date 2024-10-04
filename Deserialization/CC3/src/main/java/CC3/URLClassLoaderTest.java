package CC3;

import java.net.URL;
import java.net.URLClassLoader;

public class URLClassLoaderTest {
    public static void main(String[] args) throws Exception {
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file:///home/n0zom1z0/Evil.class")});
//        Class evilclass = urlClassLoader.loadClass("EVIL.Evil");
//        evilclass.newInstance();
        Class c = urlClassLoader.getClass();
        System.out.println(c.getName());
    }
}

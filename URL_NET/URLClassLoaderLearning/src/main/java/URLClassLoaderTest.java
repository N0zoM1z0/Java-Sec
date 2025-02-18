import java.net.URL;
import java.net.URLClassLoader;

public class URLClassLoaderTest {
    public static void main(String[] args) throws Exception {
//        FileProtocol();
        HttpProtocol();
    }
    public static void FileProtocol() throws Exception {
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file:///home/web/Desktop/Java-Sec/")});
        System.out.println("Classloader URL: " + urlClassLoader.getURLs()[0]);

        Class c = urlClassLoader.loadClass("Calc");
        c.newInstance();
    }
    public static void HttpProtocol() throws Exception {
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("http://127.0.0.1:9999/")});
        System.out.println("Classloader URL: " + urlClassLoader.getURLs()[0]);

        Class c = urlClassLoader.loadClass("Calc");
        c.newInstance();
    }
}

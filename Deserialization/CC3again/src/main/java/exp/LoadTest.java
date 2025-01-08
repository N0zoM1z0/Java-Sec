package exp;

public class LoadTest {
    public static void main(String[] args) throws Exception {
        String className = "assets.EvilClass";
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Class<?> evil = loader.loadClass(className);
        evil.newInstance();

    }
}

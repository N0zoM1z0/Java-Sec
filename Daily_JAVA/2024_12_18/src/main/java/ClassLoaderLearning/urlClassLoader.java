package ClassLoaderLearning;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

public class urlClassLoader extends ClassLoader {
    private String classUrl;

    public urlClassLoader(String classUrl) {
        this.classUrl = classUrl;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // 构造类文件的完整 URL
            URL url = new URL(classUrl + name.replace('.', '/') + ".class");
            InputStream input = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;

            // 读取类文件的字节码
            while ((length = input.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            byte[] classData = baos.toByteArray();

            // 将字节码转换为 Class 对象
            return defineClass(name, classData, 0, classData.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException(name);
        }
    }

    public static void main(String[] args) throws Exception {
        String classUrl = "http://localhost:9876/";
        urlClassLoader loader = new urlClassLoader(classUrl);

        // load class from URL
        Class<?> clazz = loader.loadClass("utils.helloworld");
        Object instance = clazz.getDeclaredConstructor().newInstance();
        System.out.println("Class loaded: " + clazz.getName());
        System.out.println("Instance created: " + instance);

        // reflect to run main in utils.helloworld
        Method method = clazz.getDeclaredMethod("main", String[].class);
        method.setAccessible(true);
        // need (Object) to wrap it !!!!!!!!!
        method.invoke(instance, (Object) (new String[]{}));
    }
}

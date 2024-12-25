package ClassLoaderLearning;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class MyClassLoader extends ClassLoader {
    private String classPath;

    public MyClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String fileName = classPath + name.replace('.', '/') + ".class";
            FileInputStream fis = new FileInputStream(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }

            // turn bytebuffer to class objective
            byte[] classData = bos.toByteArray();
            return defineClass(name, classData, 0, classData.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException(name);
        }
    }

    public static void main(String[] args) throws Exception {
        try{
            String classPath = "/home/n0zom1z0/Desktop/Java-Sec/Daily_JAVA/2024_12_18/target/classes/utils/";
            MyClassLoader loader = new MyClassLoader(classPath);
            Class<?> clazz = loader.loadClass("utils.helloworld");
            Object instance = clazz.getDeclaredConstructor().newInstance();
            System.out.println("Class loaded: " + clazz.getName());
            System.out.println("Instance created: " + instance.getClass().getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

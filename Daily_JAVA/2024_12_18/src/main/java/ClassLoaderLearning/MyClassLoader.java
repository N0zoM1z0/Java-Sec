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
        try{
            String fileName = classPath + name.replace('.', '/') + ".class";
            FileInputStream fis = new FileInputStream(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

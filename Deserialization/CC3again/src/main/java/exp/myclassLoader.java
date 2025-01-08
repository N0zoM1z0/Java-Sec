package exp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class myclassLoader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        String classpath = System.getProperty("assets.EvilClass");

    }
    public Class<?> loadClassFromFile(String filePath) throws IOException {
        // 读取字节码
        File file = new File(filePath);
        byte[] classBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(classBytes);
        }

        // 调用 defineClass 定义类
        return defineClass(null, classBytes, 0, classBytes.length);
    }
}

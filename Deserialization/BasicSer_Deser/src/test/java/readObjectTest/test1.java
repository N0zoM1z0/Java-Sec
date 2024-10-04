package readObjectTest;

import java.io.*;

public class test1 {
    public static void main(String[] args) throws Exception{
        vuln_class c = new vuln_class();
        serialize(c);
        deserialize("ser.bin");
    }
    public static void serialize(Object obj) throws Exception{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }
    public static Object deserialize(String filename) throws Exception{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
        Object obj = ois.readObject();
        return obj;
    }
}

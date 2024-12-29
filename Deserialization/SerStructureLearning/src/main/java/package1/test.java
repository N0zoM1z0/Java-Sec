package package1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class test {
    public static void main(String[] args) throws Exception {
        Person person = new Person("name",1);
        serialize(person);
        deserialize();
    }
    public static void serialize(Object obj) throws Exception{
        FileOutputStream fos = new FileOutputStream("ser.bin");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
    }
    public static Object deserialize() throws Exception{
        FileInputStream fis = new FileInputStream("ser.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }
}

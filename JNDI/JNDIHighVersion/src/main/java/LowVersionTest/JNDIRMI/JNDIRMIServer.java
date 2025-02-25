package LowVersionTest.JNDIRMI;

import javax.naming.InitialContext;
import javax.naming.Reference;

public class JNDIRMIServer {
    public static void main(String[] args) throws Exception {
        InitialContext ic = new InitialContext();
//        Registry registry = LocateRegistry.createRegistry(1099);
        Reference reference = new Reference("Calc","Calc",
                "http://localhost:8888/");
        ic.rebind("rmi://localhost:1099/sayHello",reference);
        System.out.println("Binding rmi://localhost:1099/sayHello ...");
    }
}

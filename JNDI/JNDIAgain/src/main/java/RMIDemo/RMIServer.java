package RMIDemo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws Exception {
        RemoteObj remoteObj = new RemoteObjImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("sayHello", remoteObj);
        System.out.println("RMI Server running  ...");
    }
}

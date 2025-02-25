package RMIDemo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        RemoteObj remoteObj = (RemoteObj) registry.lookup("sayHello");
        remoteObj.sayHello("Hi RMI!");
    }
}

package RMIDemo;

import java.rmi.Remote;

public interface RemoteObj extends Remote {
    public void sayHello(String things) throws Exception;
}

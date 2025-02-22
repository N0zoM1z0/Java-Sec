package RMIDemo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObjImpl extends UnicastRemoteObject implements RemoteObj {
    public RemoteObjImpl() throws RemoteException {
        super();
    }
    @Override
    public void sayHello(String things) throws Exception {
        System.out.println(things.toUpperCase());
        try {
            Runtime.getRuntime().exec("/usr/bin/gnome-calculator");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

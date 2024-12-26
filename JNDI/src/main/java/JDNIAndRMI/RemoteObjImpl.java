package JDNIAndRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObjImpl extends UnicastRemoteObject implements RemoteObj {
    public RemoteObjImpl() throws RemoteException {}

    @Override
    public String SayHello(String name) throws RemoteException {
        String upName = name.toUpperCase();
        System.out.println(upName);
        return upName;
    }
}

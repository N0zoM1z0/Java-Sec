import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIImpl extends UnicastRemoteObject implements RmiInterface{
    protected RMIImpl() throws RemoteException {
        super();
    }
    @Override
    public String test() throws RemoteException {
        return "Welcome to RMI Implementation";
    }
}

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiInterface extends Remote {
    String test() throws RemoteException;
}

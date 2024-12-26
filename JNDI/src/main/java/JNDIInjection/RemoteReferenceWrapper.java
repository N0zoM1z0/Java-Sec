package JNDIInjection;

import com.sun.jndi.rmi.registry.RemoteReference;

import javax.naming.Reference;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteReferenceWrapper extends UnicastRemoteObject implements Remote {
    private Reference reference;

    public RemoteReferenceWrapper(Reference reference) throws RemoteException {
        super();
        this.reference = reference;
    }

    public Reference getReference() {
        return reference;
    }
}

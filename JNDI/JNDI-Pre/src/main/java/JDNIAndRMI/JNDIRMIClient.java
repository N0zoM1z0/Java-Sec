package JDNIAndRMI;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.server.RemoteObject;

public class JNDIRMIClient {
    public static void main(String[] args) throws Exception {
        Context context = new InitialContext();
        RemoteObj remoteObject = (RemoteObj) context.lookup("rmi://localhost:1099/jndi");
        System.out.println(remoteObject.SayHello("hello rmi"));
    }
}

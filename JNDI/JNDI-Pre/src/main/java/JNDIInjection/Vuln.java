package JNDIInjection;

import JDNIAndRMI.JNDIRMIClient;
import JDNIAndRMI.RemoteObj;

import javax.naming.Context;
import javax.naming.InitialContext;

public class Vuln {
    public static void main(String[] args) throws Exception {
        Context ctx = new InitialContext();
        String jndiUrl = "rmi://localhost:1099/remoteObj";

        RemoteObj obj = (RemoteObj)ctx.lookup(jndiUrl);
        obj.SayHello("malicious");
        System.out.println("Received obj: " + obj);
    }
}

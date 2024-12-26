package JNDIInjection;

import JDNIAndRMI.JNDIRMIClient;
import JDNIAndRMI.RemoteObj;

import javax.naming.Context;
import javax.naming.InitialContext;

public class Vuln {
    public static void main(String[] args) throws Exception {
        Context ctx = new InitialContext();
        String jdniUrl = "rmi://localhost:1099/remoteObj";

        Object obj = (RemoteObj)ctx.lookup(jdniUrl);
        ((RemoteObj) obj).SayHello("malicious");
        System.out.println("Received obj: " + obj);
    }
}

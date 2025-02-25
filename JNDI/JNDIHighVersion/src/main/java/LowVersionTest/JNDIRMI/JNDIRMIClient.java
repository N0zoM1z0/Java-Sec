package LowVersionTest.JNDIRMI;

import LowVersionTest.RMIDemo.RemoteObj;

import javax.naming.InitialContext;

public class JNDIRMIClient {
    public static void main(String[] args) throws Exception {
        InitialContext ic = new InitialContext();
        System.out.println("Test lower version EXP ...");
        System.out.println("lookup to trigger JNDI Injection ... ");
        RemoteObj remoteObj = (RemoteObj) ic.lookup("rmi://localhost:1099/sayHello");
/*
Exception in thread "main" javax.naming.ConfigurationException: The object factory is untrusted. Set the system property 'com.sun.jndi.rmi.object.trustURLCodebase' to 'true'.
	at com.sun.jndi.rmi.registry.RegistryContext.decodeObject(RegistryContext.java:495)
	at com.sun.jndi.rmi.registry.RegistryContext.lookup(RegistryContext.java:138)
	at com.sun.jndi.toolkit.url.GenericURLContext.lookup(GenericURLContext.java:218)
	at javax.naming.InitialContext.lookup(InitialContext.java:417)
	at LowVersionTest.JNDIRMI.JNDIRMIClient.main(JNDIRMIClient.java:12)
 */
    }
}

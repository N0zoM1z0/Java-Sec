package JNDIRMI;

import RMIDemo.*;

import javax.naming.InitialContext;

public class JNDIRMIClient {
    public static void main(String[] args) throws Exception {
        InitialContext ic = new InitialContext();
        System.out.println("lookup to trigger JNDI Injection ... ");
        RemoteObj remoteObj = (RemoteObj) ic.lookup("rmi://localhost:1099/sayHello");
/*
loadClass:72, VersionHelper12 (com.sun.naming.internal)
loadClass:87, VersionHelper12 (com.sun.naming.internal)
getObjectFactoryFromReference:158, NamingManager (javax.naming.spi)
getObjectInstance:319, NamingManager (javax.naming.spi)
decodeObject:464, RegistryContext (com.sun.jndi.rmi.registry)
lookup:124, RegistryContext (com.sun.jndi.rmi.registry)
lookup:205, GenericURLContext (com.sun.jndi.toolkit.url)
lookup:417, InitialContext (javax.naming)
main:11, JNDIRMIClient (JNDIRMI)
 */
    }
}

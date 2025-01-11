package JNDIRMI;

import RMIDemo.*;

import javax.naming.InitialContext;

public class JNDIRMIClient {
    public static void main(String[] args) throws Exception {
        InitialContext ic = new InitialContext();
        RemoteObj remoteObj = (RemoteObj) ic.lookup("rmi://localhost:1099/sayHello");
        remoteObj.sayHello("Jndi WIth Rmi");
    }
}

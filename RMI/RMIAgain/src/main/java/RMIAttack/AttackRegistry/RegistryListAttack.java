package RMIAttack.AttackRegistry;

import RMIDemo.*;

import java.rmi.Naming;

public class RegistryListAttack {
    public static void main(String[] args) throws Exception{
        RemoteObj remoteObj = new RemoteObjImpl();
        String[] s = Naming.list("rmi://127.0.0.1:1099");
        System.out.println(s);
    }
}

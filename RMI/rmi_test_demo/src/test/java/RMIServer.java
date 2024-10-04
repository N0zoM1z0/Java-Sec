import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    public static final String RMI_HOST = "127.0.0.1";
    public static final int RMI_PORT = 9527;
    public static final String RMI_NAME = "rmi://" + RMI_HOST + ":" + RMI_PORT + "/test";

    public static void main(String[] args) {
        try{
            LocateRegistry.createRegistry(RMI_PORT);
            Naming.bind(RMI_NAME,new RMIImpl());
            System.out.println("[+] RMI Server Start Success! Server addr: " + RMI_NAME);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

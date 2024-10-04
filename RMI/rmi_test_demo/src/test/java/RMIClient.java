import java.rmi.Naming;

public class RMIClient {
    public static void main(String[] args) {
        try {
            RmiInterface rt = (RmiInterface) Naming.lookup("rmi://127.0.0.1:9527/test");
            String result = rt.test();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

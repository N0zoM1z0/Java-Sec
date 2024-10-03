import java.util.HashMap;

public class trigger {
    public static void main(String[] args) throws Exception {
        HashMap<Object,Object> hashMap = new HashMap<>();
        Evil evil = new Evil();
        hashMap.put(evil,"evil");
        System.out.println("[+] Hacked!");
    }
}

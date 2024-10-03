import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FileIODemo {
    public static void main(String[] args) throws Exception {
        File file = new File("/etc/passwd");
        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(file,"File");

    }
}

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RuntimeTest {
    public static void main(String[] args) throws Exception {
        Process process = Runtime.getRuntime().exec("whoami");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while( (line = br.readLine()) != null ) {
            System.out.println(line);
        }
    }
}

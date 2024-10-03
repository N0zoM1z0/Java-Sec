import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SSRF_Demo {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8080");
        URLConnection conn = url.openConnection();
        conn.connect();
        conn.getInputStream();
        StringBuilder input = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            input.append('\n').append(line);
        }
        System.out.println(input);
    }
}

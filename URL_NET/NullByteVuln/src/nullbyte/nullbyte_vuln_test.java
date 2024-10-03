package nullbyte;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class nullbyte_vuln_test {
    public static void main(String[] args) throws Exception{
        String filname = "file:///etc/passwd\u0000.jpg";
        URL url = new URL(filname);
        URLConnection conn = url.openConnection();
        conn.connect();
        conn.getInputStream();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}

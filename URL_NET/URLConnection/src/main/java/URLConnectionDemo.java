import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionDemo {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://www.baidu.com");
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(1000);
        connection.setRequestProperty("User-Agent", "N0zoM1z0");

        connection.connect();
        connection.getHeaderFields();
        connection.getInputStream();
        StringBuilder resp = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            resp.append('\n').append(inputLine);
        }
        System.out.println(resp);
    }
}

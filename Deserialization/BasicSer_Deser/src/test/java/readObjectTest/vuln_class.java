package readObjectTest;

import java.io.*;

public class vuln_class implements Serializable {
    public String name;
    public int age;

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        System.out.println("[+] HACKED!");
        Process process = Runtime.getRuntime().exec("whoami");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}

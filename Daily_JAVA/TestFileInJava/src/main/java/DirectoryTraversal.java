import java.io.File;

public class DirectoryTraversal {
    public static void main(String[] args) throws Exception {
        String directory = "/home/web/Desktop/Java-Sec/";
        String filename = "/.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./.\\./etc/passwd";
        filename = "../../../../../../../../../../etc/passwd";
        String path = directory + filename;
        String cmd = "cat " + path;
        System.out.println(cmd);
        Process p =  Runtime.getRuntime().exec(cmd);
        int c;
        while ((c = p.getInputStream().read()) != -1) {
            System.out.print((char) c);
        }
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File does not exist");
        }
        System.out.println(file.getAbsolutePath());
    }
}

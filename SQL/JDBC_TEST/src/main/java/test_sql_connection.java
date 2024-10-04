import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class test_sql_connection {
    public static void main(String[] args) throws Exception {
        String CLASS_NAME = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306";
        String USER = "root";
        String PASSWORD = "root";
        Class.forName(CLASS_NAME);
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("show databases");
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }
}

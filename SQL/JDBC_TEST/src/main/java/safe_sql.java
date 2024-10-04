import java.sql.*;

public class safe_sql {
    public static void main(String[] args) throws Exception {
        String CLASS_NAME = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/mysql";
        String USER = "root";
        String PASSWORD = "root";
        Class.forName(CLASS_NAME);
        Connection conn = null;
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();
        String user = "root '";
//        String sql = "select Host,User from mysql.user where User = '" + user + "'";
        String sql = "select Host,User from mysql.user where User = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user);
        System.out.println("[-] SQL : " + sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            System.out.print("[-] " + rs.getString("host") + " | ");
            System.out.println(rs.getString("user"));
        }
    }
}

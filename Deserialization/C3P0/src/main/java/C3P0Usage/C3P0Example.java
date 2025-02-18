package C3P0Usage;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Example {
    public static void main(String[] args) throws Exception {
        // 创建C3P0连接池
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        // 配置数据库连接池属性
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver"); // MySQL驱动
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/ofcms"); // 数据库URL
            dataSource.setUser("root"); // 数据库用户名
            dataSource.setPassword("Root123"); // 数据库密码

            // 连接池的相关配置
            dataSource.setInitialPoolSize(5); // 初始连接池大小
            dataSource.setMaxPoolSize(20); // 最大连接池大小
            dataSource.setMinPoolSize(5); // 最小连接池大小
            dataSource.setMaxIdleTime(300); // 空闲连接最大存活时间（秒）
            dataSource.setMaxConnectionAge(600); // 最大连接生命周期（秒）

            // 获取数据库连接
            Connection connection = dataSource.getConnection();

            // 使用连接执行SQL操作
            System.out.println("数据库连接成功: " + connection);

            // 关闭连接
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

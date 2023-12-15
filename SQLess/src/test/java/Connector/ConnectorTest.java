package Connector;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:57
 */
public class ConnectorTest {
    @Test
    public void TestConnector(){
        try {
            // 创建连接
            Connector connector = new Connector("mysql","127.0.0.1", 13306, "root", "123456", "TEST");

            // 创建表格（如果需要的话）
            String createTableSQL = "CREATE TABLE users (\n" +
                    "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    username VARCHAR(50) NOT NULL,\n" +
                    "    email VARCHAR(100) NOT NULL\n" +
                    ");";
            connector.execSQL(createTableSQL);
            String insertTableSQL = "INSERT INTO users (username, email) VALUES\n" +
                    "    ('user1', 'user1@example.com'),\n" +
                    "    ('user2', 'user2@example.com'),\n" +
                    "    ('user3', 'user3@example.com');\n";
            connector.execSQL(insertTableSQL);

            // 执行查询
            String querySQL = "SELECT * from users;";
            Result result = connector.execSQL(querySQL);
            querySQL = "SELECT * from users where 1";
            Result result1 = connector.execSQL(querySQL);
            System.out.println("dealing with results");
            System.out.println(result1.compare(result));

            // 处理查询结果
            result.getResult();
            result1.getResult();

            // 关闭连接
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package Strategy;

import MySql.MySqlLexer;
import MySql.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午4:11
 */
public class RemoveGroupByVisitorTest {
    @Test
    public void TestRemoveGroupByVisitor(){
        String originalSql = "SELECT department, COUNT(employee_id) AS number_of_employees\n" +
                "FROM employees\n" +
                "WHERE salary > 50000\n" +
                "GROUP BY department\n" +
                "HAVING number_of_employees > 10\n" +
                "ORDER BY number_of_employees DESC;\nSELECT\n" +
                "    product_id,\n" +
                "    DATE_FORMAT(order_date, '%Y-%m') AS month,\n" +
                "    COUNT(*) AS total_orders\n" +
                "FROM\n" +
                "    orders\n" +
                "GROUP BY\n" +
                "    product_id,\n" +
                "    DATE_FORMAT(order_date, '%Y-%m');\n"
                ;
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RemoveGroupByVisitor visitor = new RemoveGroupByVisitor(tokens,2);

        MySqlParser parser = new MySqlParser(tokens);
        visitor.visit(parser.root());

        String modifiedSql = visitor.getText();
        System.out.println(modifiedSql);

//        Assertions.assertFalse(modifiedSql.contains("GROUP"));
    }
}

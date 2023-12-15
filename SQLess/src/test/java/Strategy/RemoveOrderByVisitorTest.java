package Strategy; /**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:15
 */
import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import Strategy.RemoveOrderByVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveOrderByVisitorTest {

    private String originalSql;

    @BeforeEach
    public void setUp() {
        originalSql = "SELECT \n" +
                "    e.employee_id, \n" +
                "    e.first_name, \n" +
                "    e.last_name, \n" +
                "    (SELECT MAX(o.total_amount) \n" +
                "     FROM orders\n" +
                "     WHERE e.employee_id = o.employee_id) AS highest_order_amount\n" +
                "FROM \n" +
                "    employees e\n" +
                "WHERE \n" +
                "    e.employee_id IN (SELECT o.employee_id \n" +
                "                      FROM orders o \n" +
                "                      GROUP BY o.employee_id \n" +
                "                      HAVING COUNT(o.order_id) > 5" +
                "                      ORDER BY o.employee_id) \n" +
                "ORDER BY \n" +
                "    highest_order_amount DESC;\n";
    }

    @Test
    public void testRemoveOrderBy() {
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);

        // 解析SQL语句
        ParseTree tree = parser.root(); // 或其他适当的入口点

        // 创建并运行visitor以删除ORDER BY子句
        RemoveOrderByVisitor visitor = new RemoveOrderByVisitor(tokens,1);
        visitor.visit(tree);

        // 打印原始和修改后的SQL
        System.out.println("Original SQL: \n" + originalSql);
        System.out.println("\nModified SQL: \n" + visitor.getText());
    }
}

package Strategy; /**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:15
 */
import MySql.MySqlLexer;
import MySql.MySqlParser;
import Strategy.RemoveOrderByListener;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveOrderByListenerTest {

    private String originalSql;
    private RemoveOrderByListener listener;

    @BeforeEach
    public void setUp() {
        originalSql = "SELECT \n" +
                "    e.employee_id, \n" +
                "    e.first_name, \n" +
                "    e.last_name, \n" +
                "    (SELECT MAX(o.total_amount) \n" +
                "     FROM orders o \n" +
                "     WHERE e.employee_id = o.employee_id) AS highest_order_amount\n" +
                "FROM \n" +
                "    employees e\n" +
                "WHERE \n" +
                "    e.employee_id IN (SELECT o.employee_id \n" +
                "                      FROM orders o \n" +
                "                      GROUP BY o.employee_id \n" +
                "                      HAVING COUNT(o.order_id) > 5" +
                "                      ORDER BY o.employee_id) -- 假设这里的条件是员工必须至少有5个订单\n" +
                "ORDER BY \n" +
                "    highest_order_amount DESC;\n";
    }

    @Test
    public void testRemoveOrderBy() {
        // 生成原始的AST
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root(); // 或其他适当的入口点

        // 使用自定义listener遍历AST
        ParseTreeWalker walker = new ParseTreeWalker();
        RemoveOrderByListener listener = new RemoveOrderByListener();
        walker.walk(listener, tree);

        // 获取重写后的SQL，它将不包含ORDER BY子句
        String newSql = listener.getRewrittenSql();

        System.out.println(newSql);
    }

}

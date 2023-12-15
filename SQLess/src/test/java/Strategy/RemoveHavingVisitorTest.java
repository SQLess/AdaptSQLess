package Strategy; /**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:15
 */

import MySql.MySqlLexer;
import MySql.MySqlParser;
import Strategy.RemoveHavingVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RemoveHavingVisitorTest {

    @Test
    public void testRemoveHavingClause() {
        String originalSql = "SELECT \n" +
                "    outer_s.customer_id,\n" +
                "    outer_s.customer_name,\n" +
                "    outer_s.total_orders,\n" +
                "    outer_s.total_amount\n" +
                "FROM \n" +
                "    (SELECT \n" +
                "         c.id AS customer_id,\n" +
                "         c.name AS customer_name,\n" +
                "         COUNT(o.id) AS total_orders,\n" +
                "         SUM(o.amount) AS total_amount\n" +
                "     FROM \n" +
                "         customers AS c\n" +
                "     JOIN \n" +
                "         orders AS o ON c.id = o.customer_id\n" +
                "     WHERE \n" +
                "         o.order_date BETWEEN '2022-01-01' AND '2022-12-31'\n" +
                "     GROUP BY \n" +
                "         c.id, c.name\n" +
                "     HAVING \n" +
                "         COUNT(o.id) > 10 \n" +
                "         AND SUM(o.amount) > 5000) AS outer_s\n" +
                "WHERE \n" +
                "    outer_s.customer_id IN (SELECT \n" +
                "                                customer_id \n" +
                "                            FROM \n" +
                "                                order_details\n" +
                "                            GROUP BY \n" +
                "                                customer_id \n" +
                "                            HAVING \n" +
                "                                SUM(quantity) > 100)\n" +
                "ORDER BY \n" +
                "    outer_s.total_amount DESC;\n";

        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RemoveHavingVisitor visitor = new RemoveHavingVisitor(tokens,2);

        MySqlParser parser = new MySqlParser(tokens);
        visitor.visit(parser.root());

        String modifiedSql = visitor.getText();
        System.out.println(modifiedSql);

        // Ensure that the modified SQL does not contain "HAVING"
        Assertions.assertFalse(modifiedSql.contains("HAVING"));
    }
}

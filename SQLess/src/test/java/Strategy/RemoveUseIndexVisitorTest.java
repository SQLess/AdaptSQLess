package Strategy; /**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:19
 */
import MySql.MySqlLexer;
import MySql.MySqlParser;
import Strategy.RemoveUseIndexVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.Trees;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveUseIndexVisitorTest {

    public RemoveUseIndexVisitor visitor;
    private String originalSql;

    @BeforeEach
    void setUp() {
        originalSql = "SELECT customers.name, orders.order_date\n" +
                "FROM customers\n" +
                "USE INDEX (idx_name)\n" +
                "JOIN orders \n" +
                "ON customers.customer_id = orders.customer_id\n" +
                "WHERE orders.total > 1000;\n";
//        originalSql = "select /*+ index(emp ind_emp_sal)*/ * from emp where deptno=200 and sal>300;";
    }

    @Test
    void testRemoveUseIndex() {
        // Generate the original AST
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root();
//        System.out.println(tree.getText());       //select*fromempwheredeptno=200andsal>300;<EOF>
//        String sql = SQLConstructVisitor.getSQLfromAST((RuleNode) tree);     //select * from emp where deptno = 200 and sal > 300 ;
//        System.out.println(sql);    //select /*+ index(emp ind_emp_sal)*/ * from emp where deptno=200 and sal>300;

        // Use the custom visitor to modify the AST
        RemoveUseIndexVisitor visitor = new RemoveUseIndexVisitor(tokens);
        visitor.visit(tree);

        // Get the rewritten SQL without the USE INDEX clause
        String newSql = visitor.getModifiedText();

        System.out.println(newSql);
        assertFalse(newSql.contains("USE INDEX (idx_name)"), "The resulting SQL should not contain the 'USE INDEX (idx_name)' clause");
    }
}

package Strategy;

import MySql.MySqlLexer;
import MySql.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/11/1 下午8:03
 */
public class RemoveLogicalOpVisitorTest {
    @Test
    public void TestRemoveLogicalOpVisitor() {
        String originalSql = "select * from t where 1 and 2 or 0;";
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);

        // Parse the SQL statement
        ParseTree tree = parser.root();

        // Create and run the visitor to remove the logical operation side
        RemoveLogicalOpVisitor visitor = new RemoveLogicalOpVisitor(tokens, RemoveLogicalOpVisitor.OperandSide.LEFT, 1);
        visitor.visit(tree);

        // Print the original and modified SQL
        System.out.println("Original SQL: \n" + originalSql);
        System.out.println("\nModified SQL: \n" + visitor.getText());
    }
}

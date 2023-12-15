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
 * @date 2023/10/27 下午4:01
 */

public class RemoveLimitVisitorTest {

    @Test
    public void TestRemoveLimitVisitor(){
        String originalSql = "select * from t LIMIT 10;";
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RemoveLimitVisitor visitor = new RemoveLimitVisitor(tokens,1);

        MySqlParser parser = new MySqlParser(tokens);
        visitor.visit(parser.root());

        String modifiedSql = visitor.getText();
        System.out.println(modifiedSql);

        // Ensure that the modified SQL does not contain "HAVING"
        Assertions.assertFalse(modifiedSql.contains("LIMIT"));
    }
}

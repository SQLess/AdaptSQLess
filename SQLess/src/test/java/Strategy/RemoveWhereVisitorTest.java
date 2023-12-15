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
 * @date 2023/10/27 下午3:17
 */
public class RemoveWhereVisitorTest {
    @Test
    public void TestRemoveWhereVisitor(){
        String originalSql = "select a,(select * from s where 1) from t where t.name in (select * from s where s.a = 1);";

        // 生成原始的AST
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root(); // 或其他适当的入口点


        // 使用自定义visitor遍历并“修改”AST
        RemoveWhereVisitor visitor = new RemoveWhereVisitor(tokens,2);
        visitor.visit(tree);

        // 获取重写后的SQL，它将不包含WHERE子句
        String newSql = visitor.getText();

        System.out.println(originalSql);
        System.out.println(newSql);
    }
}

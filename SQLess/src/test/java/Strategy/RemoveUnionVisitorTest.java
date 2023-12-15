package Strategy;

import MySql.MySqlLexer;
import MySql.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午4:35
 */
public class RemoveUnionVisitorTest {
    private String originalSql;

    @BeforeEach
    public void setUp() {
        originalSql = "(SELECT first_name, last_name FROM employees WHERE department_id = 1)\n" +
                "UNION ALL\n" +
                "(SELECT first_name, last_name FROM managers WHERE location = 'New York');\n";
    }
    @Test
    public void TestRemoveUnionVisitorLeft(){
        // 生成原始的AST
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root(); // 或其他适当的入口点
        // 使用自定义visitor遍历并“修改”AST
        RemoveUnionVisitor visitor = new RemoveUnionVisitor(tokens,0);
        visitor.visit(tree);

        // 获取重写后的SQL，它将不包含WHERE子句
        String newSql = visitor.getText();

        System.out.println(originalSql);
        System.out.println(newSql);
    }

    @Test
    public void TestRemoveUnionVisitorRight(){
        // 生成原始的AST
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root(); // 或其他适当的入口点
        // 使用自定义visitor遍历并“修改”AST
        RemoveUnionVisitor visitor = new RemoveUnionVisitor(tokens,1);
        visitor.visit(tree);

        // 获取重写后的SQL，它将不包含WHERE子句
        String newSql = visitor.getText();

        System.out.println(originalSql);
        System.out.println(newSql);
    }

}

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
 * @date 2023/11/5 下午12:19
 */
public class ExtractUnionPartsVisitorTest {
    private String originalSql;

    @BeforeEach
    public void setUp() {
        originalSql = "(SELECT first_name, last_name FROM employees WHERE department_id = 1)\n" +
                "UNION DISTINCT\n" +
                "(SELECT first_name, last_name FROM managers WHERE location = 'New York');\n";
//        originalSql = "select * from t";
    }
    @Test
    public void TestExtractUnionPartsVisitor(){
        // 生成原始的AST
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root(); // 或其他适当的入口点
        // 使用自定义visitor遍历并“修改”AST
        ExtractUnionPartsVisitor visitor = new ExtractUnionPartsVisitor(tokens);
        visitor.visit(tree);

        // 获取重写后的SQL，它将不包含WHERE子句
        String leftPart = visitor.getLeftPart();
        String UnionPart = visitor.getMiddlePart();
        String rightPart = visitor.getRightPart();

        System.out.println("leftPart = " + leftPart);
        System.out.println("UnionPart = " + UnionPart);
        System.out.println("rightPart = " + rightPart);

//        System.out.println(originalSql);
    }
}

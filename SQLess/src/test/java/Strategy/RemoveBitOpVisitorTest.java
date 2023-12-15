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
 * @date 2023/11/1 下午7:45
 */
public class RemoveBitOpVisitorTest {
    @Test
    public void  TestRemoveBitOpVisitor(){
        String originalSql = "(SELECT (0&`col_bigint_undef_signed`&ADDTIME(_UTF8MB4'2017-06-19 02:05:51', _UTF8MB4'18:20:54')) AS `f1` FROM  " +
                "`table_3_utf8_undef` AS `t1` HAVING ((CHARSET(`f1`)) NOT IN (SELECT `col_float_undef_signed` FROM `table_3_utf8_undef`)));";
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);

        // 解析SQL语句
        ParseTree tree = parser.root(); // 或其他适当的入口点

        // 创建并运行visitor以删除ORDER BY子句
        RemoveBitOpVisitor visitor = new RemoveBitOpVisitor(tokens, RemoveBitOpVisitor.OperandSide.RIGHT,2);
        visitor.visit(tree);

        // 打印原始和修改后的SQL
        System.out.println("Original SQL: \n" + originalSql);
        System.out.println("\nModified SQL: \n" + visitor.getText());
    }
}

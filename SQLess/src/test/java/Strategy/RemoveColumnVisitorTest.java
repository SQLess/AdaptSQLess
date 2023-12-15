package Strategy;

import MySql.MySqlLexer;
import MySql.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/28 10:38
 */
public class RemoveColumnVisitorTest {
    private String originalSql;

    @BeforeEach
    public void setUp() {
        originalSql = "(SELECT (`f5`+`f6`>>TIMESTAMP(_UTF8MB4'2000-06-08')) AS `f2`,(0^`f5`&ADDTIME(_UTF8MB4'2017-06-19 02:05:51', _UTF8MB4'18:20:54')) AS `f1`," +
                "" +
                "(CONCAT_WS(`f4`, `f5`, `f5`)) AS `f3` " +
                "FROM (SELECT `col_float_key_unsigned` AS `f4`,`col_bigint_undef_signed` AS `f5`,`col_float_undef_signed` AS `f6` FROM `table_3_utf8_undef`) AS `t1` " +
                "HAVING (((CHARSET(`f1`)) NOT IN (SELECT `col_float_undef_signed` FROM `table_3_utf8_undef`)) IS FALSE) IS FALSE);\n";
//        originalSql  = "select f4 from t;";
        originalSql = "(SELECT (`f9`%`f8`) AS `f1`,(`f9`^~`f9`|BINARY 1) AS `f2`," +
                "(COLLATION(_UTF8MB4'2008')) AS `f3` " +
                "FROM " +
                "(SELECT `col_decimal(40, 20)_undef_unsigned` AS `f7`," +
                "`col_decimal(40, 20)_key_signed` AS `f10`,`col_decimal(40, 20)_undef_signed` AS `f9` FROM `table_3_utf8_undef`) " +
                "AS `t2` " +
                "JOIN (SELECT `col_float_key_unsigned` AS `f11`,`col_decimal(40, 20)_key_unsigned` AS `f8`," +
                "`col_double_undef_signed` AS `f12` FROM `table_3_utf8_undef` ) AS `t3`)";
    }
    @Test
    public void TestRemoveColumnVisitor(){
        // 生成原始的AST
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root(); // 或其他适当的入口点

        AliasVisitor avisitor = new AliasVisitor();
        avisitor.visit(tree);
        Graph graph = avisitor.getGraph();
        ConstructDG consDG = new ConstructDG(graph);
        consDG.visit(tree);
        graph = consDG.getGraph();
        System.out.println("-------------Before DG--------------");
        graph.printGraph();

        // 使用自定义visitor遍历并“修改”AST
        RemoveColumnVisitor visitor = new RemoveColumnVisitor(tokens,0,graph);
        visitor.visit(tree);

        // 获取重写后的SQL，它将不包含WHERE子句
        String newSql = visitor.getText();
        //获取重写后的Graph
        graph = visitor.getGraph();
        System.out.println("-------------After DG--------------");
        graph.printGraph();

        System.out.println(originalSql);
        System.out.println(newSql);
    }
}

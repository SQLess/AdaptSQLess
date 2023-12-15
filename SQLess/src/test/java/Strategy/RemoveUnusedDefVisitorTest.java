package Strategy;

import MySql.MySqlLexer;
import MySql.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/11/1 9:34
 */
public class RemoveUnusedDefVisitorTest {
    @Test
    public void TestRemoveUnusedDefVisitor(){
        String originalSql = "(SELECT (0^`f5`&ADDTIME(_UTF8MB4'2017-06-19 02:05:51', _UTF8MB4'18:20:54')) AS `f1`" +
                " FROM (SELECT `col_float_key_unsigned` AS `f4`,`col_bigint_undef_signed` AS `f5`,`col_float_undef_signed` AS `f6` FROM `table_3_utf8_undef`) AS t1 " +
                "HAVING (((CHARSET(`f1`)) NOT IN (SELECT `col_float_undef_signed` FROM `table_3_utf8_undef`)) IS FALSE) IS FALSE);";
//        originalSql = "(SELECT (`f7` DIV COERCIBILITY(`f7`)|BINARY `f8`) AS `f2`,(~CHARSET(_UTF8MB4'2011')) AS `f3` FROM " +
//                "(SELECT `col_double_undef_unsigned` AS `f10`,`col_decimal(40, 20)_key_unsigned` AS `f8`,`col_double_undef_unsigned` AS `f11` " +
//                "FROM `table_7_utf8_undef` ) AS `t2` NATURAL JOIN (SELECT (DATE_SUB(`f13`, INTERVAL 1 DAY)) AS `f7`,(DATE_SUB(-BIT_LENGTH(`f13`)," +
//                " INTERVAL 1 SECOND)) AS `f12`,(`f14`) AS `f9` FROM (SELECT `col_double_key_unsigned` AS `f13`,`col_decimal(40, 20)_undef_signed` " +
//                "AS `f14`,`col_char(20)_undef_signed` AS `f15` " +
//                "FROM `table_7_utf8_undef` FORCE INDEX (`col_double_key_signed`, `col_float_key_signed`)) AS `t3`) AS `t4`);";
//        originalSql = "(SELECT (`f7` DIV COERCIBILITY(`f7`)|BINARY `f8`) AS `f2`,(~CHARSET(_UTF8MB4'2011')) AS `f3` FROM (SELECT `col_double_undef_unsigned` AS `f10`,`col_decimal(40, 20)_key_unsigned` AS `f8`,`col_double_undef_unsigned` AS `f11` FROM `table_7_utf8_undef` ) AS `t2` NATURAL JOIN (SELECT (DATE_SUB(`f13`, INTERVAL 1 DAY)) AS `f7`,(DATE_SUB(-BIT_LENGTH(`f13`), INTERVAL 1 SECOND)) AS `f12`,(`f14`) AS `f9` FROM (SELECT `col_double_key_unsigned` AS `f13`,`col_decimal(40, 20)_undef_signed` AS `f14`,`col_char(20)_undef_signed` AS `f15` FROM `table_7_utf8_undef` FORCE INDEX (`col_double_key_signed`, `col_float_key_signed`)) AS `t3`) AS `t4`);";
        String slimoriginalSql = originalSql;
        do {
            MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(slimoriginalSql));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MySqlParser parser = new MySqlParser(tokens);
            ParseTree tree = parser.root();
            AliasVisitor visitor = new AliasVisitor();
            visitor.visit(tree);
            Graph graph = visitor.getGraph();
            ConstructDG dg = new ConstructDG(graph);
            dg.visit(tree);
            graph = dg.getGraph();
//            graph.printGraph();

            RemoveUnusedDefVisitor visitor1 = new RemoveUnusedDefVisitor(tokens, graph);
            visitor1.visit(tree);
            String newSql = visitor1.getText();
            System.out.println("==========================");
            System.out.println("newSql" + newSql);
            if(newSql.equals(slimoriginalSql)) break;
            slimoriginalSql = newSql;
        }while (true);

        System.out.println(originalSql);
        System.out.println(slimoriginalSql);
    }
}

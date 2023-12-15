package Strategy;

import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/25 19:34
 */
public class ConstructDG extends MySqlParserBaseVisitor<Void> {
    private Graph graph;
    public ConstructDG(Graph graph) {
        this.graph = graph;
        graph.resetAllNodeCounts();  //Init一下
    }
    @Override
    public Void visitTerminal(TerminalNode node) {
        String text = node.getText();
        Node graphNode = graph.getNodeByAlias(text);
        if (graphNode != null) {
            graphNode.incrementCount();
        }
        return super.visitTerminal(node);
    }

    public Graph getGraph() {
        return graph;
    }

//    public static void main(String[] args) {
//        String sql = "(SELECT (0^`f5`&ADDTIME(_UTF8MB4'2017-06-19 02:05:51', _UTF8MB4'18:20:54')) AS `f1`,(`f5`+`f6`>>TIMESTAMP(_UTF8MB4'2000-06-08')) AS `f2`,(CONCAT_WS(`f4`, `f5`, `f5`)) AS `f3` FROM (SELECT `col_float_key_unsigned` AS `f4`,`col_bigint_undef_signed` AS `f5`,`col_float_undef_signed` AS `f6` FROM `table_3_utf8_undef`) AS `t1` HAVING (((CHARSET(`f1`)) NOT IN (SELECT `col_float_undef_signed` FROM `table_3_utf8_undef`)) IS FALSE) IS FALSE);";
//
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(sql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root();
//        AliasVisitor visitor = new AliasVisitor();
//        visitor.visit(tree);
//        Graph graph = visitor.getGraph();
//        ConstructDG consDG = new ConstructDG(graph);
//        consDG.visit(tree);
//        graph = consDG.getGraph();
//        graph.printGraph();
//    }
}

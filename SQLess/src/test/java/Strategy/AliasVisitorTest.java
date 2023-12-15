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
 * @date 2023/10/27 下午3:05
 */
public class AliasVisitorTest {
    @Test
    public void TestAliasVisitor(){
        String sql = "SELECT u.id, u.name, ua.action\n" +
                "FROM users u\n" +
                "JOIN (\n" +
                "    SELECT user_id, action AS A\n" +
                "    FROM user_actions\n" +
                "    ORDER BY action_date DESC\n" +
                "    LIMIT 1\n" +
                ") AS ua ON u.id = ua.user_id;";

        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(sql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root();
        AliasVisitor visitor = new AliasVisitor();
        visitor.visit(tree);
        Graph graph = visitor.getGraph();
        graph.printGraph();
    }
}

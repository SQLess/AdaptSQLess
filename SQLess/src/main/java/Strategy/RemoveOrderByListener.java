/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:15
 */
package Strategy;

import MySql.MySqlLexer;
import MySql.MySqlParserBaseListener;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import MySql.MySqlParser;

public class RemoveOrderByListener extends MySqlParserBaseListener {
    StringBuilder rewrittenSql = new StringBuilder();
    boolean insideOrderByClause = false;

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        if (ctx instanceof MySqlParser.OrderByClauseContext) {
            insideOrderByClause = true;
        }
    }

    @Override public void visitTerminal(TerminalNode node) {
        if(insideOrderByClause==false){
            rewrittenSql.append(node.getText()+" ");
        }
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        if (ctx instanceof MySqlParser.OrderByClauseContext) {
            insideOrderByClause = false;
        }
    }


    public String getRewrittenSql() {
        return rewrittenSql.toString();
    }

//    public static void main(String[] args) {
//        String originalSql = "SELECT \n" +
//                "    e.employee_id, \n" +
//                "    e.first_name, \n" +
//                "    e.last_name, \n" +
//                "    (SELECT MAX(o.total_amount) \n" +
//                "     FROM orders o \n" +
//                "     WHERE e.employee_id = o.employee_id) AS highest_order_amount\n" +
//                "FROM \n" +
//                "    employees e\n" +
//                "WHERE \n" +
//                "    e.employee_id IN (SELECT o.employee_id \n" +
//                "                      FROM orders o \n" +
//                "                      GROUP BY o.employee_id \n" +
//                "                      HAVING COUNT(o.order_id) > 5" +
//                "                      ORDER BY o.employee_id) -- 假设这里的条件是员工必须至少有5个订单\n" +
//                "ORDER BY \n" +
//                "    highest_order_amount DESC;\n";
//        // 生成原始的AST
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root(); // 或其他适当的入口点
//
//        // 使用自定义listener遍历AST
//        ParseTreeWalker walker = new ParseTreeWalker();
//        RemoveOrderByListener listener = new RemoveOrderByListener();
//        walker.walk(listener, tree);
//
//        // 获取重写后的SQL，它将不包含ORDER BY子句
//        String newSql = listener.getRewrittenSql();
//
//        System.out.println(newSql);
//    }
}

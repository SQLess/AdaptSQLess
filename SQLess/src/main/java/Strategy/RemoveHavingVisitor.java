package Strategy;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/22 15:24
 */

import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
public class RemoveHavingVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private int index;
    private int currentCount = 0;

    public RemoveHavingVisitor(TokenStream tokens,int _index) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.index = _index;
    }

    @Override
    public Void visitHavingClause(MySqlParser.HavingClauseContext ctx) {
        currentCount++;
        if(currentCount == index) {
            rewriter.delete(ctx.getStart(), ctx.getStop());
            // 不要调用super，这样我们就不会访问这个子句的子节点
            return null;
        }else{
            return super.visitHavingClause(ctx);
        }
    }

    public String getText() {
        // 获取重写后的文本
        return rewriter.getText();
    }

//    public static void main(String[] args) {
//        String originalSql = "SELECT \n" +
//                "    outer_s.customer_id,\n" +
//                "    outer_s.customer_name,\n" +
//                "    outer_s.total_orders,\n" +
//                "    outer_s.total_amount\n" +
//                "FROM \n" +
//                "    (SELECT \n" +
//                "         c.id AS customer_id,\n" +
//                "         c.name AS customer_name,\n" +
//                "         COUNT(o.id) AS total_orders,\n" +
//                "         SUM(o.amount) AS total_amount\n" +
//                "     FROM \n" +
//                "         customers AS c\n" +
//                "     JOIN \n" +
//                "         orders AS o ON c.id = o.customer_id\n" +
//                "     WHERE \n" +
//                "         o.order_date BETWEEN '2022-01-01' AND '2022-12-31'\n" +
//                "     GROUP BY \n" +
//                "         c.id, c.name\n" +
//                "     HAVING \n" +
//                "         COUNT(o.id) > 10 \n" +
//                "         AND SUM(o.amount) > 5000) AS outer_s\n" +
//                "WHERE \n" +
//                "    outer_s.customer_id IN (SELECT \n" +
//                "                                customer_id \n" +
//                "                            FROM \n" +
//                "                                order_details\n" +
//                "                            GROUP BY \n" +
//                "                                customer_id \n" +
//                "                            HAVING \n" +
//                "                                SUM(quantity) > 100)\n" +
//                "ORDER BY \n" +
//                "    outer_s.total_amount DESC;\n";
//        // 生成原始的AST
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root(); // 或其他适当的入口点
//        // 使用自定义visitor来删除HAVING子句
//        RemoveHavingVisitor visitor = new RemoveHavingVisitor(tokens);
//        visitor.visit(tree);
//
//        // 打印原始和修改后的SQL
//        System.out.println("Original SQL: \n" + originalSql);
//        System.out.println("\nModified SQL: \n" + visitor.getText());
//    }
}


//public class RemoveHavingVisitor extends MySqlParserBaseVisitor<ParseTree> {
//    private ParserRuleContext newRoot = new ParserRuleContext();
//
//    public ParseTree visit(ParserRuleContext tree) {
//        newRoot.copyFrom(tree);
//        return tree.accept(this);
//    }
//
//    @Override
//    public ParseTree visitChildren(RuleNode node) {
//        ParseTree result = this.defaultResult();
//        int childCount = node.getChildCount();
//
//        for (int i = 0; i < childCount; ++i) {
//            ParseTree child = node.getChild(i);
//            if (child instanceof MySqlParser.HavingClauseContext) {
//                continue; // 如果是 HAVING 子句，跳过此子节点
//            }
//
//            if (child instanceof RuleContext) {
//                ParserRuleContext temp = new ParserRuleContext();
//                temp.copyFrom((ParserRuleContext)child);
//                newRoot.addChild(temp);
//            } else if (child instanceof TerminalNode) {
//                TerminalNodeImpl temp = new TerminalNodeImpl(((TerminalNode) child).getSymbol());
//                newRoot.addChild(temp);
//            }
//            // 否则，正常访问子节点
//            ParseTree visitedChild = child.accept(this); // 访问子节点
//            result = this.aggregateResult(result,visitedChild);
//        }
//        return result;
//    }
//
//    public ParserRuleContext getNewRoot() {
//        return newRoot;
//    }
//
//    public static void main(String[] args) {
//        String originalSql = "SELECT \n" +
//                "    outer_s.customer_id,\n" +
//                "    outer_s.customer_name,\n" +
//                "    outer_s.total_orders,\n" +
//                "    outer_s.total_amount\n" +
//                "FROM \n" +
//                "    (SELECT \n" +
//                "         c.id AS customer_id,\n" +
//                "         c.name AS customer_name,\n" +
//                "         COUNT(o.id) AS total_orders,\n" +
//                "         SUM(o.amount) AS total_amount\n" +
//                "     FROM \n" +
//                "         customers AS c\n" +
//                "     JOIN \n" +
//                "         orders AS o ON c.id = o.customer_id\n" +
//                "     WHERE \n" +
//                "         o.order_date BETWEEN '2022-01-01' AND '2022-12-31'\n" +
//                "     GROUP BY \n" +
//                "         c.id, c.name\n" +
//                "     HAVING \n" +
//                "         COUNT(o.id) > 10 \n" +
//                "         AND SUM(o.amount) > 5000) AS outer_s\n" +
//                "WHERE \n" +
//                "    outer_s.customer_id IN (SELECT \n" +
//                "                                customer_id \n" +
//                "                            FROM \n" +
//                "                                order_details\n" +
//                "                            GROUP BY \n" +
//                "                                customer_id \n" +
//                "                            HAVING \n" +
//                "                                SUM(quantity) > 100)\n" +
//                "ORDER BY \n" +
//                "    outer_s.total_amount DESC;\n";
//        // 生成原始的AST
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root(); // 或其他适当的入口点
//
//        // 使用自定义visitor遍历并“修改”AST
//        RemoveHavingVisitor visitor = new RemoveHavingVisitor();
////        MySqlParserBaseVisitor visitor = new MySqlParserBaseVisitor();
//        visitor.visit(tree);
////        ParserRuleContext newTree = visitor.getNewRoot();
////        System.out.println(visitor.modifiedChildren);
////        visitor.visit(tree);
//        RuleNode newTree = visitor.getNewRoot();
//
//        // 获取重写后的SQL，它将不包含ORDER BY子句
//        String newSql = SQLConstructVisitor.getSQLfromAST((RuleNode) newTree);
//
//        System.out.println(originalSql);
//        System.out.println(newSql);
//    }
//}

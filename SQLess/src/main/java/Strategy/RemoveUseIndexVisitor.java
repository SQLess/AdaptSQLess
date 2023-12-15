package Strategy;
/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:15
 */

import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class RemoveUseIndexVisitor extends MySqlParserBaseVisitor<Void> {

    private final TokenStreamRewriter rewriter;

    public RemoveUseIndexVisitor(TokenStream tokens) {
        this.rewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public Void visitIndexHint(MySqlParser.IndexHintContext ctx) {
        // If it's a USE INDEX hint, remove it.
//        if (ctx.indexHintAction.getText().toUpperCase().equals("USE") &&
//                ctx.keyFormat.getText().toUpperCase().equals("INDEX")) {
//            rewriter.delete(ctx.getStart(), ctx.getStop());
//        }
        rewriter.delete(ctx.getStart(),ctx.getStop());
        return super.visitIndexHint(ctx);
    }

    public String getModifiedText() {
        return rewriter.getText();
    }

//    public static void main(String[] args) {
//        String originalSql = "SELECT customers.name, orders.order_date\n" +
//                "FROM customers\n" +
//                "USE INDEX (idx_name)\n" +
//                "JOIN orders \n" +
//                "ON customers.customer_id = orders.customer_id\n" +
//                "WHERE orders.total > 1000;\n";
//
//        // Generate the original AST
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root();
//
//        // Use the custom visitor to modify the AST
//        RemoveUseIndexVisitor visitor = new RemoveUseIndexVisitor(tokens);
//        visitor.visit(tree);
//
//        // Get the rewritten SQL without the USE INDEX clause
//        String newSql = visitor.getModifiedText();
//
//        System.out.println(originalSql);
//        System.out.println(newSql);
//    }
}


//package Strategy;
//
//import MySql.MySqlLexer;
//import MySql.MySqlParser;
//import MySql.MySqlParserBaseVisitor;
//import org.antlr.v4.runtime.*;
//import org.antlr.v4.runtime.tree.*;
//
//public class RemoveUseIndexVisitor extends MySqlParserBaseVisitor<ParseTree> {
//    private ParserRuleContext newRoot = new ParserRuleContext();
//
//    public ParseTree visit(ParserRuleContext tree) {
//        newRoot.copyFrom(tree);
//        return tree.accept(this);
//    }
//
//    @Override
//    public ParseTree visitIndexHint(MySqlParser.IndexHintContext ctx) {
//        // If it's a USE INDEX hint, skip it.
//        if (ctx.indexHintAction.getText().toUpperCase().equals("USE") &&
//                ctx.keyFormat.getText().toUpperCase().equals("INDEX")) {
//            return null;
//        }
//
//        return super.visitIndexHint(ctx);
//    }
//
//    @Override
//    public ParseTree visitChildren(RuleNode node) {
//        ParseTree result = this.defaultResult();
//        int childCount = node.getChildCount();
//
//        for (int i = 0; i < childCount; ++i) {
//            ParseTree child = node.getChild(i);
//
//            if (child instanceof RuleContext) {
//                ParserRuleContext temp = new ParserRuleContext();
//                temp.copyFrom((ParserRuleContext) child);
//                newRoot.addChild(temp);
//            } else if (child instanceof TerminalNode) {
//                TerminalNodeImpl temp = new TerminalNodeImpl(((TerminalNode) child).getSymbol());
//                newRoot.addChild(temp);
//            }
//
//            ParseTree visitedChild = child.accept(this); // Visit child
//            result = this.aggregateResult(result, visitedChild);
//        }
//
//        return result;
//    }
//
//    public ParserRuleContext getNewRoot() {
//        return newRoot;
//    }
//
//    public static void main(String[] args) {
//        String originalSql = "SELECT customers.name, orders.order_date\n" +
//                "FROM customers\n" +
//                "USE INDEX (idx_name)\n" +
//                "JOIN orders \n" +
//                "ON customers.customer_id = orders.customer_id\n" +
//                "WHERE orders.total > 1000;\n";
//        // 生成原始的AST
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root(); // 或其他适当的入口点
//
//        // 使用自定义visitor遍历并“修改”AST
//        RemoveUseIndexVisitor visitor = new RemoveUseIndexVisitor();
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
//
//}

//Another method
/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:15
 */
/**
 * 要修改代码以删除SQL查询中的"ORDER BY"子句，你可以选择使用TokenStreamRewriter，
 * 它是ANTLR提供的一个类，用于在不直接修改原始TokenStream的情况下重写TokenStream。
 * TokenStreamRewriter允许你指定要删除的标记范围或要插入的新标记，然后创建一个新的文本输出，反映了这些更改。
 */
package Strategy;

import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
public class RemoveOrderByVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private int index;
    private int currentCount = 0;

    public RemoveOrderByVisitor(TokenStream tokens,int _index) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.index = _index;
    }

    @Override
    public Void visitOrderByClause(MySqlParser.OrderByClauseContext ctx) {
        currentCount++;
        if(currentCount == index) {
            // 在访问ORDER BY子句时，使用rewriter删除整个子句。
            rewriter.delete(ctx.getStart(), ctx.getStop());
            // 不要调用super，这样我们就不会访问这个子句的子节点。
            return null;
        }else{
            return super.visitOrderByClause(ctx);
        }
    }

    public String getText() {
        // 获取重写后的文本
        return rewriter.getText();
    }

}

//package Strategy;
//
//import MySql.MySqlLexer;
//import MySql.MySqlParser;
//import MySql.MySqlParserBaseVisitor;
//import org.antlr.v4.runtime.*;
//import org.antlr.v4.runtime.tree.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RemoveOrderByVisitor extends MySqlParserBaseVisitor<ParseTree> {
////    List<ParseTree> modifiedChildren = new ArrayList<>();
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
//            if (child instanceof MySqlParser.OrderByClauseContext) {
//                continue; // 如果是 ORDER BY 子句，跳过此子节点
//            }
//
////            modifiedChildren.add(child);
//            if (child instanceof RuleContext) {
////                newRoot.addChild((RuleContext) child);
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
//
//    public static void main(String[] args) {
//        String originalSql = "(SELECT (DATE_SUB(_UTF8MB4'2003-07-17 13:23:30', INTERVAL 1 DAY_MICROSECOND)) AS `f1`,(_UTF8MB4'2016-01-19'-CHARSET(`f6`)) AS `f2`,(DATE_SUB(SUBDATE(_UTF8MB4'2000-09-11', INTERVAL 1 HOUR_MINUTE), INTERVAL 1 MICROSECOND)) AS `f3` FROM (SELECT `col_double_undef_signed` AS `f4`,`col_float_undef_signed` AS `f5`,`col_decimal(40, 20)_key_signed` AS `f6` FROM `table_7_utf8_undef` IGNORE INDEX (`col_varchar(20)_key_signed`, `col_float_key_unsigned`)) AS `t1` HAVING ((CAST((COERCIBILITY(_UTF8MB4'2002-11-23 23:55:07')) AS CHAR) REGEXP _UTF8MB4'[a-z]$') IS TRUE) AND (CAST((`f2`) AS CHAR) LIKE _UTF8MB4'%1%')) UNION ALL (SELECT (`f9`%`f8`) AS `f1`,(`f9`^~`f9`|BINARY 1) AS `f2`,(COLLATION(_UTF8MB4'2008')) AS `f3` FROM (SELECT `col_decimal(40, 20)_undef_unsigned` AS `f7`,`col_decimal(40, 20)_key_signed` AS `f10`,`col_decimal(40, 20)_undef_signed` AS `f9` FROM `table_3_utf8_undef`) AS `t2` JOIN (SELECT `col_float_key_unsigned` AS `f11`,`col_decimal(40, 20)_key_unsigned` AS `f8`,`col_double_undef_signed` AS `f12` FROM `table_3_utf8_undef` USE INDEX (`col_bigint_key_signed`)) AS `t3`)";
//        // 生成原始的AST
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root(); // 或其他适当的入口点
//
//        // 使用自定义visitor遍历并“修改”AST
//        RemoveOrderByVisitor visitor = new RemoveOrderByVisitor();
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
//
//}



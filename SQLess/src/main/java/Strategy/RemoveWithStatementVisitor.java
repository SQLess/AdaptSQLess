package Strategy;
/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/22 15:51
 */

import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import MySql.MySqlLexer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
public class RemoveWithStatementVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private MySqlParser.RootContext myctx;
    private boolean isWith = false;
    private boolean modify = false;

    public RemoveWithStatementVisitor(CommonTokenStream tokens) {
        this.rewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public Void visitRoot(MySqlParser.RootContext ctx) {
        this.myctx = ctx;
        return super.visitRoot(ctx);
    }

    @Override
    public Void visitWithStatement(MySqlParser.WithStatementContext ctx) {
        isWith = true;
        return visitChildren(ctx);
    }

    @Override
    public Void visitCommonTableExpressions(MySqlParser.CommonTableExpressionsContext ctx) {
        // 保留dmlStatement部分，删除其他部分
        if(isWith && !modify){
            rewriter.delete(myctx.getStart().getTokenIndex(),ctx.dmlStatement().getStart().getTokenIndex()-1);
            rewriter.delete(ctx.dmlStatement().getStop().getTokenIndex()+1, myctx.getStop().getTokenIndex());
            modify = true;
        }
        return null;
    }

    public String getText(){
        return rewriter.getText();
    }

//    public static void main(String[] args) {
//        String originalSql = "WITH `MYWITH` AS ((SELECT (0^`f5`&ADDTIME(_UTF8MB4'2017-06-19 02:05:51', _UTF8MB4'18:20:54')) AS `f1`,(`f5`+`f6`>>TIMESTAMP(_UTF8MB4'2000-06-08')) AS `f2`,(CONCAT_WS(`f4`, `f5`, `f5`)) AS `f3` FROM (SELECT `col_float_key_unsigned` AS `f4`,`col_bigint_undef_signed` AS `f5`,`col_float_undef_signed` AS `f6` FROM `table_3_utf8_undef` USE INDEX (`col_bigint_key_unsigned`, `col_bigint_key_signed`)) AS `t1` HAVING (((CHARSET(`f1`)) NOT IN (SELECT `col_float_undef_signed` FROM `table_3_utf8_undef` USE INDEX (`col_decimal(40, 20)_key_signed`, `col_decimal(40, 20)_key_signed`))) IS FALSE) IS FALSE ORDER BY `f5`) UNION (SELECT (BINARY COS(0)|1) AS `f1`,(!1) AS `f2`,(LOWER(`f9`)) AS `f3` FROM (SELECT `col_decimal(40, 20)_key_unsigned` AS `f7`,`col_bigint_key_unsigned` AS `f8`,`col_bigint_key_signed` AS `f9` FROM `table_3_utf8_undef` IGNORE INDEX (`col_decimal(40, 20)_key_unsigned`, `col_varchar(20)_key_signed`)) AS `t2` WHERE (((DATE_ADD(_UTF8MB4'16:47:10', INTERVAL 1 MONTH)) IN (SELECT `col_decimal(40, 20)_key_unsigned` FROM `table_3_utf8_undef`)) OR ((ROW(`f8`,DATE_SUB(BINARY LOG2(8572968212617203413), INTERVAL 1 HOUR_SECOND)) IN (SELECT `col_bigint_key_unsigned`,`col_decimal(40, 20)_undef_unsigned` FROM `table_7_utf8_undef` USE INDEX (`col_double_key_unsigned`, `col_decimal(40, 20)_key_unsigned`))) IS FALSE) OR ((`f7`) BETWEEN `f7` AND `f9`)) IS TRUE ORDER BY `f7`)) SELECT * FROM `MYWITH`;"; // 示例SQL
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root(); // 或其他适当的入口点
//
//        RemoveWithStatementVisitor visitor = new RemoveWithStatementVisitor(tokens);
//        visitor.visit(tree);
//
//        String newSql = visitor.getText();
//        System.out.println(originalSql);
//        System.out.println(newSql);
//    }
}












//public class RemoveWithStatementVisitor extends MySqlParserBaseVisitor<ParseTree> {
//    private ParserRuleContext newRoot = new ParserRuleContext();
//    boolean isWith = false;
//    boolean isdml = false;
////    private TokenStreamRewriter rewriter;
//
////    public RemoveWithStatementVisitor(CommonTokenStream tokens) {
////        this.rewriter = new TokenStreamRewriter(tokens);
////    }
//
//    @Override
//    public ParseTree visitWithStatement(MySqlParser.WithStatementContext ctx) {
//        // 查找commonTableExpressions
//        for (ParseTree child : ctx.children) {
//            if (child instanceof MySqlParser.CommonTableExpressionsContext) {
//                isWith = true; // 设置标志为true，表示我们正在访问With子树
////                newRoot = new ParserRuleContext();
////                newRoot.copyFrom((ParserRuleContext)child); // 或者 newRoot.addChild(child);
//                child.accept(this); // 访问此子树，将其中的节点添加到newRoot
//                isWith = false; // 重置标志
//                break; // 如果您只想要第一个commonTableExpression，否则请删除此行以添加所有commonTableExpressions
//            }
//        }
//        return newRoot; // 返回新的根节点
//    }
//
//    @Override
//    public ParseTree visitWithClause(MySqlParser.WithClauseContext ctx) {
//        // 查找commonTableExpressions
//        for (ParseTree child : ctx.children) {
//            if (child instanceof MySqlParser.CommonTableExpressionsContext) {
//                isWith = true; // 设置标志为true，表示我们正在访问With子树
////                newRoot = new ParserRuleContext();
////                newRoot.copyFrom((ParserRuleContext)child); // 或者 newRoot.addChild(child);
//                child.accept(this); // 访问此子树，将其中的节点添加到newRoot
//                isWith = false; // 重置标志
//                break; // 如果您只想要第一个commonTableExpression，否则请删除此行以添加所有commonTableExpressions
//            }
//        }
//        return newRoot; // 返回新的根节点
//    }
//
//    @Override
//    public ParseTree visitCommonTableExpressions(MySqlParser.CommonTableExpressionsContext ctx) {
//        // 在CommonTableExpressions中找到dmlStatement
//        for (ParseTree child : ctx.children) {
//            if (child instanceof MySqlParser.DmlStatementContext && isWith==true) {
//                isdml = true;
//                int startIndex = ((MySqlParser.DmlStatementContext) child).getStart().getTokenIndex();
//                int endIndex = ((MySqlParser.DmlStatementContext) child).getStop().getTokenIndex();
////                rewriter.delete(0,startIndex);
////                rewriter.delete(endIndex,rewriter.getLastRewriteTokenIndex());
//
//                newRoot = new ParserRuleContext();
//                newRoot.copyFrom((ParserRuleContext) child);
//                child.accept(this); // 访问此子树，将其中的节点添加到newRoot
//                isdml = false;
//                // 不再继续访问其它子节点，因为我们已找到我们需要的部分
//                break;
//            }
//        }
//        return newRoot; // 返回新的根节点，即dmlStatement的内容
//    }
//
////    public String getText() {
////        // 获取重写后的文本
////        return rewriter.getText();
////    }
//
//    @Override
//    public ParseTree visitChildren(RuleNode node) {
//        if (!isdml) { // 如果当前不在With子树内，则正常遍历子节点
//            return super.visitChildren(node);
//        }
//
//        // 如果在With子树内，则将子节点添加到newRoot
//        ParseTree result = this.defaultResult();
//        int childCount = node.getChildCount();
//        for (int i = 0; i < childCount; ++i) {
//            ParseTree child = node.getChild(i);
//            if (child instanceof RuleContext) {
//                ParserRuleContext temp = new ParserRuleContext();
//                temp.copyFrom((ParserRuleContext)child);
//                newRoot.addChild(temp);
//            } else if (child instanceof TerminalNode) {
//                TerminalNodeImpl temp = new TerminalNodeImpl(((TerminalNode) child).getSymbol());
//                newRoot.addChild(temp);
//            }
//            ParseTree visitedChild = child.accept(this); // 访问子节点
//            result = this.aggregateResult(result, visitedChild);
//        }
//        return result;
//    }
//
//    public ParserRuleContext getNewRoot() {
//        return newRoot;
//    }
//
////    public static void main(String[] args) {
////        String originalSql = "WITH `MYWITH` AS ((SELECT (0^`f5`&ADDTIME(_UTF8MB4'2017-06-19 02:05:51', _UTF8MB4'18:20:54')) AS `f1`,(`f5`+`f6`>>TIMESTAMP(_UTF8MB4'2000-06-08')) AS `f2`,(CONCAT_WS(`f4`, `f5`, `f5`)) AS `f3` FROM (SELECT `col_float_key_unsigned` AS `f4`,`col_bigint_undef_signed` AS `f5`,`col_float_undef_signed` AS `f6` FROM `table_3_utf8_undef` USE INDEX (`col_bigint_key_unsigned`, `col_bigint_key_signed`)) AS `t1` HAVING (((CHARSET(`f1`)) NOT IN (SELECT `col_float_undef_signed` FROM `table_3_utf8_undef` USE INDEX (`col_decimal(40, 20)_key_signed`, `col_decimal(40, 20)_key_signed`))) IS FALSE) IS FALSE ORDER BY `f5`) UNION (SELECT (BINARY COS(0)|1) AS `f1`,(!1) AS `f2`,(LOWER(`f9`)) AS `f3` FROM (SELECT `col_decimal(40, 20)_key_unsigned` AS `f7`,`col_bigint_key_unsigned` AS `f8`,`col_bigint_key_signed` AS `f9` FROM `table_3_utf8_undef` IGNORE INDEX (`col_decimal(40, 20)_key_unsigned`, `col_varchar(20)_key_signed`)) AS `t2` WHERE (((DATE_ADD(_UTF8MB4'16:47:10', INTERVAL 1 MONTH)) IN (SELECT `col_decimal(40, 20)_key_unsigned` FROM `table_3_utf8_undef`)) OR ((ROW(`f8`,DATE_SUB(BINARY LOG2(8572968212617203413), INTERVAL 1 HOUR_SECOND)) IN (SELECT `col_bigint_key_unsigned`,`col_decimal(40, 20)_undef_unsigned` FROM `table_7_utf8_undef` USE INDEX (`col_double_key_unsigned`, `col_decimal(40, 20)_key_unsigned`))) IS FALSE) OR ((`f7`) BETWEEN `f7` AND `f9`)) IS TRUE ORDER BY `f7`)) SELECT * FROM `MYWITH`;";
////        originalSql = "select * from t;";
////        // 生成原始的AST
////        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
////        CommonTokenStream tokens = new CommonTokenStream(lexer);
////        MySqlParser parser = new MySqlParser(tokens);
////        ParseTree tree = parser.root(); // 或其他适当的入口点
////
////        // 使用自定义visitor遍历并“修改”AST
////        RemoveWithStatementVisitor visitor = new RemoveWithStatementVisitor();
//////        MySqlParserBaseVisitor visitor = new MySqlParserBaseVisitor();
////        visitor.visit(tree);
//////        ParserRuleContext newTree = visitor.getNewRoot();
//////        System.out.println(visitor.modifiedChildren);
//////        visitor.visit(tree);
////        RuleNode newTree = visitor.getNewRoot();
////
////        // 获取重写后的SQL，它将不包含ORDER BY子句
////        String newSql = SQLConstructVisitor.getSQLfromAST((RuleNode) newTree);
////
////        System.out.println(originalSql);
////        System.out.println(newSql);
////    }
//}

package Oracle;

import Connector.Connector;
import Connector.Result;
import Connector.SqlFileParser;
import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import MySql.MySqlParser.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class PinoloVisitor<T> extends MySqlParserBaseVisitor {

    private TokenStream tokens;

    public int depth = 0;
    /**
     * flag = 1, positive.
     */
    public int flag = 1;
    public PinoloVisitor(TokenStream _tokens) {
        this.tokens = _tokens;
        Candidate.clear();
    }

    @Override
    public T visitChildren(RuleNode node) {
        depth += 1;
        String indent = "";
        for (int i = 0; i < depth; i++) {
            indent += "  |";
        }
//        System.out.print(indent + node.getClass().getName());

        int oldFlag = flag;

        if (node instanceof ExpressionContext) {
            if (node instanceof NotExpressionContext) {
                flag ^= 1;
            } else if (node instanceof IsExpressionContext) {
                IsExpressionContext isExpr = (IsExpressionContext)node;
                if (isExpr.UNKNOWN() != null) {
                    System.out.println();
                    return null;
                }
                if (isExpr.FALSE() != null) {
                    flag ^= 1;
                }
                if (isExpr.NOT() != null) {
                    flag ^= 1;
                }
            }
        }
        if (node instanceof PredicateContext) {
            if (!(node instanceof ExpressionAtomPredicateContext)) {
                return null;
            }
        }
        if (node instanceof ExpressionAtomContext) {
            if ((node instanceof NestedExpressionAtomContext) ||
                    (node instanceof ExistsExpressionAtomContext) ||
                    (node instanceof SubqueryExpressionAtomContext)) {
            } else {
//                System.out.println();
                return null;
            }
        }

        if (node instanceof QuerySpecificationContext) {  //Distinct
            QuerySpecificationContext selectNode = (QuerySpecificationContext)node;
            List<MySqlParser.SelectSpecContext> selectspec = selectNode.selectSpec();
            boolean hasDistinct = false;
            for (MySqlParser.SelectSpecContext spec : selectspec) {
                if ("DISTINCT".equalsIgnoreCase(spec.getText())) {
                    hasDistinct = true;
                    break;
                }
            }
            if (!hasDistinct) {  //判断一下有没有DISTINCT
                Candidate.candidates.add(distinctLower(selectNode, tokens, flag));
//                TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
//                System.out.println("******************INSERT DISTINCT*****************");
//                rewriter.insertAfter(selectNode.SELECT().getSymbol(), " DISTINCT ");
//                System.out.println(rewriter.getText());
            } else{
                for (MySqlParser.SelectSpecContext spec : selectspec) {
                    if ("DISTINCT".equalsIgnoreCase(spec.getText())) {
                        Candidate.candidates.add(distinctUpper(spec, tokens, flag));
//                        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
//                        rewriter.delete(spec.getStart().getTokenIndex(), spec.getStop().getTokenIndex());
//                        System.out.println("******************DELETE DISTINCT*****************");
//                        System.out.println(rewriter.getText());
                    }
                }
            }
        } else if (node instanceof FromClauseContext) {   //FromClause
            FromClauseContext fromNode = (FromClauseContext)node;
            if (fromNode.whereExpr != null) {
                Candidate.candidates.add(whereUpper(fromNode, tokens, flag));
                Candidate.candidates.add(whereLower(fromNode, tokens, flag));
            }
        }


//        System.out.print(" [flag="+flag+"]");
//        System.out.println();

        T result = (T) super.visitChildren(node);

        flag = oldFlag;

        depth -= 1;

        return result;
    }

    Candidate whereUpper(FromClauseContext fromNode, TokenStream tokens, int flag) {
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        rewriter.replace(fromNode.expression().start,fromNode.expression().getStop(),1);
        return new Candidate("WhereUpper", 1, flag, rewriter.getText());
    }

    Candidate whereLower(FromClauseContext fromNode, TokenStream tokens, int flag) {
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        rewriter.replace(fromNode.expression().start,fromNode.expression().getStop(),0);
        return new Candidate("WhereLower", 0, flag, rewriter.getText());
    }

    Candidate distinctUpper(SelectSpecContext spec, TokenStream tokens, int flag) {
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        rewriter.delete(spec.getStart().getTokenIndex(), spec.getStop().getTokenIndex());
        return new Candidate("distinctUpper", 1, flag, rewriter.getText());
    }

    Candidate distinctLower(QuerySpecificationContext spec, TokenStream tokens, int flag) {
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        rewriter.insertAfter(spec.SELECT().getSymbol(), " DISTINCT ");
        return new Candidate("distinctLower", 0, flag, rewriter.getText());
    }

    public static void initDB(Connector connector) throws IOException {
//        // Delete table
//        connector.execSQL("DROP TABLE IF EXISTS test;");
//        // 创建表格（如果需要的话）
//        String createTableSQL = "CREATE TABLE test (\n" +
//                "    c1 INT,\n" +
//                "    c2 VARCHAR(50));";
//        connector.execSQL(createTableSQL);
//        String insertTableSQL = "INSERT INTO test VALUES\n" +
//                "    (1, 'test1'),\n" +
//                "    (2, 'test2'),\n" +
//                "    (3, 'test3');\n";
//        connector.execSQL(insertTableSQL);

        SqlFileParser p = new SqlFileParser("/home/ll5000/Desktop/db 快捷方式/SQLess/src/main/java/Bug/output.data.sql");
        p.parse();
        List<String> initsqls = p.getResult();
        for(int i=0;i<initsqls.size();i++){
            String initsql = initsqls.get(i);
            System.out.println(initsql);
            connector.execSQL(initsql);
        }
    }

    public static void main(String[] args) throws Exception {

//        String originalSql = "SELECT * FROM t WHERE !(( ((NOT 1+1) IS FALSE) && ((1+1 IS NOT FALSE) IS TRUE) ) IS NOT TRUE)";
        String originalSql = "SELECT * FROM (SELECT * FROM t WHERE 0) AS t1 WHERE 0";
        originalSql = "SELECT c1 FROM test WHERE c1 > 0;";
        originalSql = "(SELECT (WEEKOFYEAR(_UTF8MB4'2013-03-26')-0) AS `f1`,(TO_BASE64(`f5`)) AS `f2`,(DATE_SUB(~OCT(`f4`), INTERVAL 1 SECOND_MICROSECOND)) AS `f3` FROM (SELECT `col_char(20)_key_signed` AS `f4`,`col_float_undef_signed` AS `f5`,`col_bigint_undef_unsigned` AS `f6` FROM `table_7_utf8_undef` IGNORE INDEX (`col_char(20)_key_signed`)) AS `t1`) UNION (SELECT (DATE_SUB(LEFT(`f8`, 5), INTERVAL 1 YEAR_MONTH)) AS `f1`,(`f7` DIV COERCIBILITY(`f7`)|BINARY `f8`) AS `f2`,(~CHARSET(_UTF8MB4'2011')) AS `f3` FROM (SELECT `col_double_undef_unsigned` AS `f10`,`col_decimal(40, 20)_key_unsigned` AS `f8`,`col_double_undef_unsigned` AS `f11` FROM `table_7_utf8_undef` USE INDEX (`col_varchar(20)_key_signed`, `col_double_key_signed`)) AS `t2` NATURAL JOIN (SELECT (DATE_SUB(`f13`, INTERVAL 1 DAY)) AS `f7`,(DATE_SUB(-BIT_LENGTH(`f13`), INTERVAL 1 SECOND)) AS `f12`,(`f14`) AS `f9` FROM (SELECT `col_double_key_unsigned` AS `f13`,`col_decimal(40, 20)_undef_signed` AS `f14`,`col_char(20)_undef_signed` AS `f15` FROM `table_7_utf8_undef` FORCE INDEX (`col_double_key_signed`, `col_float_key_signed`)) AS `t3`) AS `t4`);";
        // 生成原始的AST
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root(); // 或其他适当的入口点

        PinoloVisitor visitor = new PinoloVisitor(tokens);
        visitor.visit(tree);


        System.out.println("==================================================");

        // 创建连接
        Connector connector = new Connector("mysql","127.0.0.1", 13306, "root", "123456", "TEST3");
        initDB(connector);

        System.out.println("[originalSql]:" + originalSql);
        Result originalRes = connector.execSQL(originalSql);
        System.out.println("[originalRes]:");
        System.out.println(originalRes);

        System.out.println("Candidates:");
        for (Candidate candidate : Candidate.candidates) {
            System.out.println("==================================================");
            System.out.println("[mutateName] " + candidate.mutateName);
            System.out.println("[isUpper] " + candidate.isUpper);
            System.out.println("[flag] " + candidate.flag);
            System.out.println("[mutateSql] " + candidate.mutateSql);
            Result mutateRes = connector.execSQL(candidate.mutateSql);
            System.out.println("[mutateRes]:");
            System.out.println(mutateRes);

            // Check
            int relation = originalRes.compare(mutateRes);
            int finalIsUpper = (candidate.isUpper ^ candidate.flag) ^ 1;

            if(OracleChecker.Pinolo_check(originalRes,mutateRes,finalIsUpper==1)){
                System.out.println("Not a bug");
            }else{
                System.out.println("Is Bug");
            }
        }

        System.out.println("==================================================");

        // 关闭连接
        connector.close();
    }

}
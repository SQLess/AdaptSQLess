/**
 * @author lilin
 * @version 1.0
 * @date 2023/9/21 9:49
 */
package org.antlr.v4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.ast.GrammarRootAST;

import java.io.IOException;
import java.util.*;

import static org.antlr.v4.Util.getSqlsFromDirectory;


public class MyTool extends Tool {
    public static Grammar lastProcessGrammar;

    public MyTool(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        Tool antlr = new MyTool(args);
        if ( args.length == 0 ) { antlr.help(); antlr.exit(0); }

        try {
            antlr.processGrammarsOnCommandLine();  //生成解析器和词法分析器代码
            System.out.println(lastProcessGrammar.fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( antlr.log ) {
                try {
                    String logname = antlr.logMgr.save();
                    System.out.println("wrote "+logname);
                }
                catch (IOException ioe) {
                    antlr.errMgr.toolError(ErrorType.INTERNAL_ERROR, ioe);
                }
            }
        }
        if ( antlr.return_dont_exit ) return;

        if (antlr.errMgr.getNumErrors() > 0) {
            antlr.exit(1);
        }
        antlr.exit(0);
    }

    @Override
    public void processGrammarsOnCommandLine() throws IOException {
        List<GrammarRootAST> sortedGrammars = sortGrammarByTokenVocab(grammarFiles);

        for (GrammarRootAST t : sortedGrammars) {
            final Grammar g = createGrammar(t);
            g.fileName = t.fileName;
            lastProcessGrammar = g;
        }
        /**
         * Step0:获取MySql的所有规则
         */
        List<Rule> rules = returng4();

        /**
         * Step1:输入需要学习的sql语句
         */
        String directoryPath = "evaluate/postgres_examples";  // 替换为您的SQL文件夹路径
        List<String> allSqlStatements = new ArrayList<>();
        Set<Rule> uniqueRules = new HashSet<>();
        allSqlStatements = getSqlsFromDirectory(directoryPath);
        for (String sql : allSqlStatements) {
            /**
             * Step 2：如果解析失败，则需要创建新的规则
             * Step 3: 将新的规则和原先的规则进行归约
             */
            List<Rule> mergerule = learning_new_rule(sql,rules);
            if(mergerule!=null) {
                uniqueRules.addAll(mergerule);
            }
        }
        /**
         * Step 4:将新的规则写入g4文件
         */
        String newFilePath = "g4/MySqlParser_new.g4";
        GrammarUpdater.updateGrammarFile(lastProcessGrammar.fileName,newFilePath,uniqueRules);

        test(rules);
    }

    public List<Rule> learning_new_rule(String sql,List<Rule> rules){
        List<Rule> result = new ArrayList<>();
        MySql.MySqlLexer lexer = new MySql.MySqlLexer(CharStreams.fromString(sql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySql.MySqlParser parser = new MySql.MySqlParser(tokens);

        CustomErrorListener errorListener = new CustomErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.root();
//        parser.setErrorHandler(new BailErrorStrategy()); //关闭错误恢复模式
//        System.out.println(tree.toStringTree(parser));

        /**
         * Step 2：如果解析失败，则需要创建新的规则，获取解析失败的栈顶信息，以及token流的名字
         * 从Parser Tree中创建
         */
        if (errorListener.hasError()) {
            System.out.println("Failed sql : " + sql);
            List<String> failedRules = errorListener.getTopRules();
            System.out.println("Failed Rules : ");
            for(String s : failedRules)
                System.out.println("        " + s);
            FailedRuleVisitor failedRuleVisitor = new FailedRuleVisitor(failedRules, parser);
            failedRuleVisitor.visit(tree);  // tree 是你的 ParseTree 对象

            Map<String, List<String>> failedRuleChildrenMap = failedRuleVisitor.getFailedRuleChildrenMap();

            for (String failedRule : failedRules) {
                List<String> tokenNames = failedRuleChildrenMap.get(failedRule);

                String ruleDefinition = String.join(" ", tokenNames);
                Rule newRule = new Rule(failedRule, ruleDefinition);

                System.out.println("Created new rules: " + newRule);

                /**
                 * Step 3: 将新的规则和原先的规则进行归约
                 */
                Rule mergerule = Rule.Reduction(rules,newRule);
                result.add(mergerule);
                System.out.println("Mergerule : " + mergerule);
            }
        }
        return result;
    }


    public List<Rule> returng4(){
        GrammarASTVisitor visitor = new GrammarASTVisitor();
        List<Rule> rules = visitor.generateGrammarAndRules(lastProcessGrammar.ast);
        return rules;
    }

    public void test(List<Rule> rules){

//        String sql = "select distinct * from t;";  //增加
//        String sql = "select from t;";  //减少
//        String sql = "";   //修改关键字

//        String sql = "INSERT INTO base_tbl SELECT i, 'Row ' || i FROM generate_series(-2, 2) g(i);";
//        String sql = "CREATE TABLE ttable1 OF nothing;";
//        String sql = "CREATE DOMAIN testxmldomain AS varchar;";

        String sql = "create table parted_trig1 partition of parted_trig for values in (1);";
        learning_new_rule(sql, rules);
    }
}
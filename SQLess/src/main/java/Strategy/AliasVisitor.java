package Strategy;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/19 13:20
 */
import MySql.MySqlLexer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;

import java.util.HashMap;
import java.util.Map;

public class AliasVisitor extends MySqlParserBaseVisitor<Void> {
    private Map<String, String> aliasMapping = new HashMap<>();
    private Graph graph = new Graph();  // 创建图实例

    private void processAlias(String realname, String alias, String Type) {
        aliasMapping.put(alias, realname);

        // 在图中添加节点和边
        Node realNode = graph.getNodeByAlias(alias);
        if (realNode == null) {
            realNode = new Node(alias, realname, Type);
            graph.addNode(realNode);
        }
    }

    @Override
    public Void visitSingleDeleteStatement(MySqlParser.SingleDeleteStatementContext ctx) {
        // 检查是否存在AS关键字以及其后的uid
        if (ctx.uid() != null) {
            String alias = ctx.uid().getText();
            String realname = ctx.tableName().getText();
//            aliasMapping.put(alias,realname);
            processAlias(realname, alias,Node.USE_TABLE);
        }
        return super.visitSingleDeleteStatement(ctx);
    }

    @Override
    public Void visitHandlerOpenStatement(MySqlParser.HandlerOpenStatementContext ctx) {
        if (ctx.uid() != null) {
            String alias = ctx.uid().getText();
            String realname = ctx.tableName().getText();
            processAlias(realname, alias,Node.USE_TABLE);
        }
        return super.visitHandlerOpenStatement(ctx);
    }

    @Override
    public Void visitSingleUpdateStatement(MySqlParser.SingleUpdateStatementContext ctx) {
        if (ctx.uid() != null) {
            String alias = ctx.uid().getText();
            String realname = ctx.tableName().getText();
            processAlias(realname, alias,Node.USE_TABLE);
        }
        return super.visitSingleUpdateStatement(ctx);
    }

    @Override
    public Void visitAtomTableItem(MySqlParser.AtomTableItemContext ctx) {
        if (ctx.alias != null) {
            String alias = ctx.alias.getText();
            String realname = ctx.tableName() != null ? ctx.tableName().getText() : "subquery"; // 如果是子查询，实际名称可以标记为"subquery"或更复杂的逻辑
            processAlias(realname, alias,Node.USE_TABLE);
        }
        return super.visitAtomTableItem(ctx);
    }



    @Override
    public Void visitSelectColumnElement(MySqlParser.SelectColumnElementContext ctx) {
        if (ctx.uid() != null) {
            String alias = ctx.uid().getText();
            String realname = ctx.fullColumnName().getText();
            processAlias(realname, alias,Node.USE_COLUMN);
        }
        return super.visitSelectColumnElement(ctx);
    }

    @Override
    public Void visitLockTableElement(MySqlParser.LockTableElementContext ctx) {
        if (ctx.uid() != null) {
            String alias = ctx.uid().getText();
            String realname = ctx.tableName().getText();
            processAlias(realname, alias,Node.USE_TABLE);
        }
        return super.visitLockTableElement(ctx);
    }

    /**
     * 处理函数别名关系、可以省略
     * @param ctx
     * @return
     */
    @Override
    public Void visitSelectFunctionElement(MySqlParser.SelectFunctionElementContext ctx) {
        if (ctx.uid() != null) {
            String alias = ctx.uid().getText();
            String realname = ctx.functionCall().getText(); // 假设functionCall是一个能够返回完整函数调用字符串的方法
            processAlias(realname, alias,Node.FUCTION);
        }
        return super.visitSelectFunctionElement(ctx);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * -------------上面处理表名、列名、函数的def关系，不需要递归查找，---------------------------------------------------------------
     * -------------下面处理表达式、子查询的def关系，需要考虑嵌套关系，-----------------------------------------------------
     * -----------------------------------------------------------------------------------------------------------------
     */


    /**
     * tableSourceItem  代表着一种查询结果集
     * @param ctx
     * @return
     * example: SELECT e.name FROM (SELECT name as n FROM employees WHERE department_id = 1) AS e;
     * tableSourceItem   :
     *    | (
     *       selectStatement
     *       | '(' parenthesisSubquery=selectStatement ')'
     *       )
     *       AS? alias=uid                                                 #subqueryTableItem
     */
    @Override
    public Void visitSubqueryTableItem(MySqlParser.SubqueryTableItemContext ctx) {
        String alias = ctx.alias.getText();
        String realName = ctx.selectStatement().getText();  // 获取parenthesisSubquery的内容作为realName

        // 使用realName和alias调用processAlias方法
        processAlias(realName, alias, Node.QUERYEXPRESSION);

        Node aliasNode = graph.getNodeByAlias(alias);

        // 在递归遍历子查询之前，保存aliasMapping的当前状态
        Map<String, String> previousAliasMapping = new HashMap<>(aliasMapping);

        // 递归遍历子查询
        visit(ctx.selectStatement());

        // 创建一个局部的别名映射
        Map<String, String> localAliasMapping = new HashMap<>();

        // 检查aliasMapping中哪些条目是新添加的，并将它们添加到localAliasMapping中
        for (Map.Entry<String, String> entry : aliasMapping.entrySet()) {
            if (!previousAliasMapping.containsKey(entry.getKey())) {
                localAliasMapping.put(entry.getKey(), entry.getValue());
            }
        }

        // 连接子查询别名和子查询中的其他别名
        for (String innerAlias : localAliasMapping.keySet()) {
            Node innerNode = graph.getNodeByAlias(innerAlias);
            if (innerNode != null) {
                graph.addEdge(aliasNode, innerNode);
            }
        }
        return null;
    }


    /**
     * 处理表达式别名
     * @param ctx
     * @return  expression里面一般不定义别名
     * @example: SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM users;
     * selectElement
     *     | (LOCAL_ID VAR_ASSIGN)? expression (AS? uid)?                  #selectExpressionElement
     */
    @Override
    public Void visitSelectExpressionElement(MySqlParser.SelectExpressionElementContext ctx) {
        String alias = ctx.uid().getText();
        String realName = ctx.expression().getText(); // 获取表达式的文本形式作为realName

        // 使用realName和alias调用processAlias方法
        processAlias(realName, alias, Node.EXPRESSION);

        Node aliasNode = graph.getNodeByAlias(alias);

        // 在递归遍历表达式之前，保存aliasMapping的当前状态
        Map<String, String> previousAliasMapping = new HashMap<>(aliasMapping);

        // 递归遍历表达式
        visit(ctx.expression());

        // 创建一个局部的别名映射
        Map<String, String> localAliasMapping = new HashMap<>();

        // 检查aliasMapping中哪些条目是新添加的，并将它们添加到localAliasMapping中
        for (Map.Entry<String, String> entry : aliasMapping.entrySet()) {
            if (!previousAliasMapping.containsKey(entry.getKey())) {
                localAliasMapping.put(entry.getKey(), entry.getValue());
            }
        }

        // 连接表达式别名和表达式中的其他别名
        for (String innerAlias : localAliasMapping.keySet()) {
            Node innerNode = graph.getNodeByAlias(innerAlias);
            if (innerNode != null) {
                graph.addEdge(aliasNode, innerNode);
            }
        }
        return null;
    }

    /**
     * 处理LATERAL查询结果集  可能有问题，不常用
     * @param ctx
     * @return
     * lateralStatement
     *     : LATERAL (querySpecificationNointo |
     *                queryExpressionNointo |
     *                ('(' (querySpecificationNointo | queryExpressionNointo) ')' (AS? uid)?)
     *               )
     *     ;
     */
    @Override
    public Void visitLateralStatement(MySqlParser.LateralStatementContext ctx) {
        String alias;
        String realName;
        if (ctx.uid() != null) {
            alias = ctx.uid().getText();
        } else {
            // 如果LATERAL子句没有明确的别名，可以为其生成一个默认别名或使用其他策略。
            alias = "lateral_default_alias"; // 这只是一个示例，你可以选择其他名称或策略。
        }

        if (ctx.querySpecificationNointo() != null) {
            realName = ctx.querySpecificationNointo().getText();
        } else if (ctx.queryExpressionNointo() != null) {
            realName = ctx.queryExpressionNointo().getText();
        } else {
            realName = "unknown"; // 这只是一个默认值，你可以选择其他策略。
        }

        // 使用realName和alias调用processAlias方法
        processAlias(realName, alias, Node.QUERYEXPRESSION);

        Node aliasNode = graph.getNodeByAlias(alias);

        // 在递归遍历LATERAL子句之前，保存aliasMapping的当前状态
        Map<String, String> previousAliasMapping = new HashMap<>(aliasMapping);

        // 递归遍历LATERAL子句
        super.visitChildren(ctx);

        // 创建一个局部的别名映射
        Map<String, String> localAliasMapping = new HashMap<>();

        // 检查aliasMapping中哪些条目是新添加的，并将它们添加到localAliasMapping中
        for (Map.Entry<String, String> entry : aliasMapping.entrySet()) {
            if (!previousAliasMapping.containsKey(entry.getKey())) {
                localAliasMapping.put(entry.getKey(), entry.getValue());
            }
        }

        // 连接LATERAL别名和LATERAL子句中的其他别名
        for (String innerAlias : localAliasMapping.keySet()) {
            Node innerNode = graph.getNodeByAlias(innerAlias);
            if (innerNode != null) {
                graph.addEdge(aliasNode, innerNode);
            }
        }
        return null;
    }

    public Graph getGraph(){
        return graph;
    }

    public Map<String, String> getAliasMapping() {
        return aliasMapping;
    }

//    public static void main(String[] args) {
//        String sql = "SELECT u.id, u.name, ua.action\n" +
//                "FROM users u\n" +
//                "JOIN (\n" +
//                "    SELECT user_id, action AS A\n" +
//                "    FROM user_actions\n" +
//                "    ORDER BY action_date DESC\n" +
//                "    LIMIT 1\n" +
//                ") AS ua ON u.id = ua.user_id;";
//
//        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(sql));
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        MySqlParser parser = new MySqlParser(tokens);
//        ParseTree tree = parser.root();
//        AliasVisitor visitor = new AliasVisitor();
//        visitor.visit(tree);
//        Graph graph = visitor.graph;
//        graph.printGraph();
////        Map<String, String> m = visitor.getAliasMapping();
////        for (Map.Entry<String, String> entry : m.entrySet()) {
////            System.out.println(entry.getKey() + " ---> " + entry.getValue());
////        }
//    }
}

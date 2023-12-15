package Strategy;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/28 10:37
 */
import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.Map;

public class RemoveColumnVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private int columnIndex;
    private Graph graph;    //前面别名分析、依赖分析构造好的依赖图
    private AliasVisitor aliasVisitor;  //用于搜集别名分析的Visitor
    private Map<String, String> aliasesInColumn;  // 存储列中定义的别名
    private boolean isOutermostSelect = true;  // 标志是否为最外层的SELECT

    public RemoveColumnVisitor(TokenStream tokens, int columnIndex, Graph graph) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.columnIndex = columnIndex;
        this.aliasVisitor = new AliasVisitor();
        this.graph = graph;
    }

    /**
     * 删除SelectElements的第N个元素
     *
     * @param ctx
     * @return 0 预处理，处理一些特殊情况，例如SELECT *、 超出索引范围等等
     * 1 找到要删除的selectElement子树，进行别名分析，依赖分析，判断是否能删
     * 2 分情况删除SelectElement
     * 3 删除后需要更新DGraph
     * 3.1 从DGraph删除已经被remove的def
     * 3.2 更新Use
     */


    @Override
    public Void visitSelectElements(MySqlParser.SelectElementsContext ctx) {
        /**
         *  0 预处理，处理一些特殊情况
         */
        if (!isOutermostSelect) {
            return null;
        }
        isOutermostSelect = false;

        if (ctx.star != null) {  // 处理 SELECT * 的情况
            return null;
        }

        if (columnIndex < 0 || columnIndex >= ctx.selectElement().size()) {  // 索引超出范围
            return null;
        }

        /**
         * 1 判断是否能删除
         */

        // 1.1  找到要删除的selectElement子树，进行别名分析，找到所有别名
        MySqlParser.SelectElementContext columnToBeRemoved = ctx.selectElement(columnIndex);
        aliasVisitor.visit(columnToBeRemoved);
        aliasesInColumn = aliasVisitor.getAliasMapping();  // 获取该子树中定义的所有别名
//        System.out.println(aliasesInColumn);

        // 1.2 从依赖图中获取该别名的结点以及它的所有可达的别名结点,如果有def，且该结点的Use>1(后面会引用）,则该列不能删除
        for(String alias:aliasesInColumn.keySet()){
            Node node = graph.getNodeByAlias(alias);
            if(node!=null  && node.getCount()>1 ){
                return null;
            }
        }

        /**
         * 2 分情况删除SelectElement
         * 如果有多个列，需要正确处理逗号
         */
        if (ctx.selectElement().size() > 1) {
            if (columnIndex == 0) {  // 删除第一个列和其后的逗号
                TokenStream tokens = rewriter.getTokenStream();
                int commaIndex = tokens.get(ctx.selectElement(1).getStart().getTokenIndex() - 1).getType();  //找到逗号的位置
                if (commaIndex == MySqlLexer.COMMA) {
                    rewriter.delete(columnToBeRemoved.getStart(), tokens.get(ctx.selectElement(1).getStart().getTokenIndex() - 1));
                } else {
                    rewriter.delete(columnToBeRemoved.getStart(), columnToBeRemoved.getStop());
                }
            } else {  //删除列和其前的逗号
                TokenStream tokens = rewriter.getTokenStream();
                int commaIndex = tokens.get(columnToBeRemoved.getStart().getTokenIndex() - 1).getType();  //找到逗号的位置
                if (commaIndex == MySqlLexer.COMMA) {  // 删除逗号和当前要删除的列
                    rewriter.delete(tokens.get(columnToBeRemoved.getStart().getTokenIndex() - 1), columnToBeRemoved.getStop());
                } else {
                    rewriter.delete(columnToBeRemoved.getStart(), columnToBeRemoved.getStop());  // 如果前一个token不是逗号，则只删除当前列
                }
//                rewriter.delete(ctx.selectElement(columnIndex - 1).getStop(), columnToBeRemoved.getStop());
            }
        } else {
            return null;   //如果只有一个列，直接返回
        }

        /**
         * 3 删除后需要更新依赖图
         */
        // 3.1 从DG中删除已经remove的结点
        for (String alias : aliasesInColumn.keySet()) {
            Node node = graph.getNodeByAlias(alias);
            if (node != null) {
                graph.removeNode(node);
            }
        }
        //3.2  更新DGraph的use计数器
        String rewritesql = getText();
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(rewritesql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        ParseTree tree = parser.root();

        ConstructDG dg = new ConstructDG(graph);
        dg.visit(tree);
        graph = dg.getGraph();

        return null;
    }

    public Graph getGraph() {
        return graph;
    }

    public String getText() {
        return rewriter.getText();
    }
}
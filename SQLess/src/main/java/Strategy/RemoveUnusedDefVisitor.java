package Strategy;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/11/1 9:31
 */
import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.Set;

public class RemoveUnusedDefVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private boolean modified = false;
    private Graph graph;
    private Set<String> unusedAliases;
    private boolean isSelectElement= false;  // 标志是否为最外层的SELECT
    private int SelectElementCount = 0;  //记录一下第几次出现SelectElemnt

    public RemoveUnusedDefVisitor(CommonTokenStream tokens, Graph graph) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.graph = graph;
        unusedAliases = graph.getNodesWithCountOne();   // 找到graph中计数器为1的结点加入到Set中
    }

    @Override
    public Void visitSelectElements(MySqlParser.SelectElementsContext ctx) {
        isSelectElement = true;
        SelectElementCount ++;
        super.visitSelectElements(ctx);
        isSelectElement = false;
        return null;
    }


    @Override
    public Void visitTerminal(TerminalNode node) {
        if (modified) {
            return null;
        }

        String text = node.getText();
        if(unusedAliases.contains(text)){
            ParseTree parent = node.getParent();
            while (parent != null && !hasSiblingWithText(parent, "AS")) {
                parent = parent.getParent();
            }
            ParserRuleContext grandParent = (ParserRuleContext)parent;
            if (grandParent != null) {
                safelyDelete(grandParent, rewriter.getTokenStream());
            }
        }
        return super.visitTerminal(node);
    }

    private void safelyDelete(ParserRuleContext grandParent, TokenStream tokens) {
        if (isSelectElement && SelectElementCount==1) {
            return;
        }

        int startIdx = grandParent.getStart().getTokenIndex();
        int stopIdx = grandParent.getStop().getTokenIndex();

        // 如果是列表中的第一个元素并且其后有逗号
        if (stopIdx + 1 < tokens.size() && tokens.get(stopIdx + 1).getType() == MySqlLexer.COMMA) {
            rewriter.delete(startIdx, stopIdx + 1);  // 删除定义及其后的逗号
            modified = true;
        }
        // 如果不是列表中的第一个元素
        else if (startIdx - 1 >= 0 && tokens.get(startIdx - 1).getType() == MySqlLexer.COMMA) {
            rewriter.delete(startIdx - 1, stopIdx);  // 删除定义及其前的逗号
            modified = true;

        } else {
            return ;
        }
    }

    private boolean hasSiblingWithText(ParseTree node, String text) {
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChild(i).getText().equals(text)) {
                return true;
            }
        }
        return false;
    }

    public String getText() {
        return rewriter.getText();
    }
}
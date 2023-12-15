package Strategy;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午3:09
 */
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

/**
 * fromClause
 *     : (FROM tableSources)?
 *       (WHERE whereExpr=expression)?
 *     ;
 */
public class RemoveWhereVisitor extends MySqlParserBaseVisitor<Void> {

    public TokenStreamRewriter rewriter;
    private int index;
    private int currentCount = 0;

    public RemoveWhereVisitor(TokenStream tokens,int _index) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.index = _index;
    }

    @Override
    public Void visitFromClause(MySqlParser.FromClauseContext ctx) {
        currentCount++;
        if(currentCount == index){
            if (ctx.WHERE() != null) {
                // 如果存在WHERE关键字，删除整个WHERE子句
                rewriter.delete(ctx.WHERE().getSymbol(), ctx.expression().getStop());
            }
            return visitChildren(ctx); // 继续访问其他子节点
        }else{
            return super.visitFromClause(ctx);
        }
    }

    public String getText() {
        // 获取重写后的文本
        return rewriter.getText();
    }

//     覆盖visitChildren方法，让它不再访问已被删除的子节点
    @Override
    public Void visitChildren(RuleNode node) {
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = node.getChild(i);
            if (!(child instanceof MySqlParser.ExpressionContext && node instanceof MySqlParser.FromClauseContext)) {
                child.accept(this);
            }
        }
        return null;
    }
}

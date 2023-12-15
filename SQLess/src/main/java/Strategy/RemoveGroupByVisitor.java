package Strategy;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午4:10
 */

import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class RemoveGroupByVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private int index;
    private int currentCount = 0;

    public RemoveGroupByVisitor(TokenStream tokens,int _index) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.index = _index;
    }

    @Override
    public Void visitGroupByClause(MySqlParser.GroupByClauseContext ctx) {
        currentCount++;
        if(currentCount==index){
            rewriter.delete(ctx.getStart(), ctx.getStop());
            return null;  //// 不要调用super，这样我们就不会访问这个子句的子节点。
        }else{
            return super.visitGroupByClause(ctx);
        }
    }

    public String getText() {
        return rewriter.getText();
    }
}

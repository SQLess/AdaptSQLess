package Strategy;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午4:00
 */

import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class RemoveLimitVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private int index;
    private int currentCount = 0;

    public RemoveLimitVisitor(TokenStream tokens,int _index) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.index = _index;
    }

    @Override
    public Void visitLimitClause(MySqlParser.LimitClauseContext ctx) {
        currentCount++;
        if(currentCount == index) {
            rewriter.delete(ctx.getStart(), ctx.getStop());
            return null;
        }else{
            return super.visitLimitClause(ctx);
        }
    }

    public String getText() {
        return rewriter.getText();
    }
}

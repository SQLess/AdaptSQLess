package Strategy;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/11/1 下午7:58
 */

import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

/**
 *  LogicalOperator
 */
public class RemoveLogicalOpVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private final OperandSide side;
    private int index;
    private int currentCount = 0;

    public enum OperandSide {  // Enum to specify which side of the binary operation you want to remove
        LEFT, RIGHT
    }

    public RemoveLogicalOpVisitor(TokenStream tokens, OperandSide side, int _index) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.side = side;
        this.index = _index;
    }

    @Override
    public Void visitLogicalExpression(MySqlParser.LogicalExpressionContext ctx) {
        currentCount++;
        if (currentCount == index) {
            switch (side) {
                case LEFT:
                    rewriter.delete(ctx.expression(0).getStart(), ctx.logicalOperator().getStart());  // Delete the left operand up to the logicalOperator
                    break;
                case RIGHT:
                    rewriter.delete(ctx.logicalOperator().getStop(), ctx.expression(1).getStop());  // Delete the logicalOperator up to the end of the right operand
                    break;
            }
            return null;
        } else {
            return super.visitLogicalExpression(ctx);
        }
    }

    public String getText() {
        return rewriter.getText();
    }
}

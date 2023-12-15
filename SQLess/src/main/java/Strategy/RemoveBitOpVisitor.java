package Strategy;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/11/1 下午7:31
 */

import MySql.MySqlLexer;
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
/**
 * bitOperator
 */
public class RemoveBitOpVisitor extends MySqlParserBaseVisitor<Void> {
    private TokenStreamRewriter rewriter;
    private final OperandSide side;
    private int index;
    private int currentCount = 0;

    public enum OperandSide {  // Enum to specify which side of the binary operation you want to remove
        LEFT, RIGHT
    }

    public RemoveBitOpVisitor(TokenStream tokens, OperandSide side,int _index) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.side = side;
        this.index = _index;
    }

    @Override
    public Void visitBitExpressionAtom(MySqlParser.BitExpressionAtomContext ctx) {
        currentCount++;
        if (currentCount == index) {
            switch (side) {
                case LEFT:
                    rewriter.delete(ctx.left.getStart(), ctx.bitOperator().getStop());  // Delete the left operand up to the bitOperator
                    break;
                case RIGHT:
                    rewriter.delete(ctx.bitOperator().getStart(), ctx.right.getStop());  // Delete the bitOperator up to the end of the right operand
                    break;
            }
            return null;
        }else{
            return super.visitBitExpressionAtom(ctx);
        }
    }

    public String getText() {
        return rewriter.getText();
    }
}

package Strategy;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午4:34
 */

import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

public class RemoveUnionVisitor extends MySqlParserBaseVisitor<Void> {

    private TokenStreamRewriter rewriter;
    private int side; // 0 for left, 1 for right

    public RemoveUnionVisitor(TokenStream tokens, int partToRemove) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.side = partToRemove;
    }

    @Override
    public Void visitUnionSelect(MySqlParser.UnionSelectContext ctx) {
        Token unionToken;
        Token unionTypeToken = null; // This will store the unionType token if it exists

        // Check if the UNION is directly within the UnionSelectContext
        if(ctx.UNION() != null){  //处理不带括号的情况
            unionToken = ctx.UNION().getSymbol();
            if (ctx.unionType != null) {
                unionTypeToken = ctx.unionType;
            }
        } else {
            MySqlParser.UnionStatementContext unionStmt = ctx.unionStatement(0);
            if(unionStmt != null) {
                unionToken = unionStmt.UNION().getSymbol();
                if (unionStmt.unionType != null) {
                    unionTypeToken = unionStmt.unionType;
                }
            } else {
                // If we still can't find the UNION, then there's an unexpected structure
                throw new RuntimeException("Unexpected structure: UNION keyword not found.");
            }
        }

        if (side == 0) {
            // Remove the left side of UNION
            if (unionTypeToken != null) {
                // If there's a unionType, delete up to that token
                rewriter.delete(ctx.getStart(), unionTypeToken);
            } else {
                // Otherwise, just delete up to the UNION token
                rewriter.delete(ctx.getStart(), unionToken);
            }
        } else if (side == 1) {
            // Remove the right side of UNION
            rewriter.delete(unionToken, ctx.getStop());
        }
        return null;
    }


    @Override
    public Void visitUnionParenthesisSelect(MySqlParser.UnionParenthesisSelectContext ctx) {
        Token unionToken;
        Token unionTypeToken = null; // This will store the unionType token if it exists

        // Check if the UNION is directly within the UnionParenthesisSelectContext
        if (ctx.UNION() != null) {
            unionToken = ctx.UNION().getSymbol();
            if (ctx.unionType != null) {
                unionTypeToken = ctx.unionType;
            }
        } else {
            // If not, then it should be within the unionParenthesis
            MySqlParser.UnionParenthesisContext unionParenthesis = null;
            for (ParseTree child : ctx.children) {
                if (child instanceof MySqlParser.UnionParenthesisContext) {
                    unionParenthesis = (MySqlParser.UnionParenthesisContext) child;
                    break;
                }
            }

            if (unionParenthesis != null) {
                unionToken = unionParenthesis.UNION().getSymbol();
                if (unionParenthesis.unionType != null) {
                    unionTypeToken = unionParenthesis.unionType;
                }
            } else {
                // If we still can't find the UNION, then there's an unexpected structure
                throw new RuntimeException("Unexpected structure: UNION keyword not found.");
            }
        }

        if (side == 0) {
            // Remove the left side of UNION
            if (unionTypeToken != null) {
                // If there's a unionType, delete up to that token
                rewriter.delete(ctx.queryExpressionNointo().getStart(), unionTypeToken);
            } else {
                // Otherwise, just delete up to the UNION token
                rewriter.delete(ctx.queryExpressionNointo().getStart(), unionToken);
            }
        } else if (side == 1) {
            // Remove the right side of UNION
            rewriter.delete(unionToken, ctx.getStop());
        }
        return null;
    }



    public String getText() {
        return rewriter.getText();
    }
}

package Strategy;

/**
 * @author LiLin
 * @version 1.0
 * @date 2023/11/5 下午12:07
 */
import MySql.MySqlParser;
import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;


/**
 *  leftRewriter is used to capture the query before the UNION.
 *  middleRewriter captures the UNION keyword and its type if it's there.
 *  rightRewriter captures the query after the UNION.
 */
public class ExtractUnionPartsVisitor extends MySqlParserBaseVisitor<Void> {

    private TokenStreamRewriter leftRewriter;
    private TokenStreamRewriter middleRewriter;
    private TokenStreamRewriter rightRewriter;
    private boolean isContainsUnion = false;

    private TokenStreamRewriter rewriter;

    public ExtractUnionPartsVisitor(TokenStream tokens) {
        this.rewriter = new TokenStreamRewriter(tokens);
        this.leftRewriter = new TokenStreamRewriter(tokens);
        this.middleRewriter = new TokenStreamRewriter(tokens);
        this.rightRewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public Void visitUnionSelect(MySqlParser.UnionSelectContext ctx) {
        isContainsUnion = true;
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
                isContainsUnion = false;
                return null;
//                throw new RuntimeException("Unexpected structure: UNION keyword not found.");
            }
        }

        // Capture the UNION keyword and any associated type modifiers with middleRewriter
        if (unionTypeToken != null) {
            middleRewriter.delete(ctx.getStart().getTokenIndex(),unionToken.getTokenIndex()-1);
            middleRewriter.delete(unionTypeToken.getTokenIndex()+1,ctx.getStop().getTokenIndex());
        }else{
            middleRewriter.replace(ctx.start, ctx.stop, unionToken.getText());
        }


        // Remove the left side of UNION
        if (unionTypeToken != null) {
            // If there's a unionType, delete up to that token
            rightRewriter.delete(ctx.getStart(), unionTypeToken);
        } else {
            // Otherwise, just delete up to the UNION token
            rightRewriter.delete(ctx.getStart(), unionToken);
        }

        // Remove the right side of UNION
        leftRewriter.delete(unionToken, ctx.getStop());

        return null;
    }


    @Override
    public Void visitUnionParenthesisSelect(MySqlParser.UnionParenthesisSelectContext ctx) {
        isContainsUnion = true;
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
                isContainsUnion = false;
                return null;
//                throw new RuntimeException("Unexpected structure: UNION keyword not found.");
            }
        }

        // Capture the UNION keyword and any associated type modifiers with middleRewriter
        if (unionTypeToken != null) {
            middleRewriter.delete(ctx.getStart().getTokenIndex(),unionToken.getTokenIndex()-1);
            middleRewriter.delete(unionTypeToken.getTokenIndex()+1,ctx.getStop().getTokenIndex());
        }else{
            middleRewriter.replace(ctx.start, ctx.stop, unionToken.getText());
        }


        // Remove the left side of UNION
        if (unionTypeToken != null) {
            // If there's a unionType, delete up to that token
            rightRewriter.delete(ctx.queryExpressionNointo().getStart(), unionTypeToken);
        } else {
            // Otherwise, just delete up to the UNION token
            rightRewriter.delete(ctx.queryExpressionNointo().getStart(), unionToken);
        }

        // Remove the right side of UNION
        leftRewriter.delete(unionToken, ctx.getStop());


        return null;
    }

    public String getLeftPart() {
        return leftRewriter.getText();
    }

    public String getMiddlePart() {
        return middleRewriter.getText();
    }

    public String getRightPart() {
        return rightRewriter.getText();
    }

    public boolean isContainsUnion(){
        return this.isContainsUnion;
    }
}


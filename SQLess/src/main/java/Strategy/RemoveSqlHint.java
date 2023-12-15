package Strategy;

import MySql.MySqlLexer;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/31 14:56
 */
import org.antlr.v4.runtime.*;


public class RemoveSqlHint {

    public static String removeOptimizationHints(CommonTokenStream tokens) {
        TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);
        tokens.fill();
        for (Token token : tokens.getTokens()) {
            // Check if the token is an optimization hint.
            if (token.getType() == MySqlLexer.COMMENT_INPUT && token.getText().startsWith("/*+") && token.getText().endsWith("*/")) {
                // Delete the token using rewriter.
                rewriter.delete(token);
            }
        }

        return rewriter.getText();
    }


    /**
     * 打印所有的tokens类型和字段
     * @param tokens
     */
    public static void printAllTokens(CommonTokenStream tokens) {
        tokens.fill();   //要拉入内存才能打印
        for (Token token : tokens.getTokens()) {
            int type = token.getType();
            String text = token.getText();
            System.out.println("Type: " + type + ", Text: " + text);
        }
    }
}

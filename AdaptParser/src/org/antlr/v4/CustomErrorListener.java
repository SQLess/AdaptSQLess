package org.antlr.v4;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/9/21 20:29
 */
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomErrorListener extends BaseErrorListener {
    private List<String> allTokens = new ArrayList<>();
//    private String topRule;
    private List<String> topRules = new ArrayList<>();
    private boolean hasError = false;
    private String offendingTokenName;
//    private ParserRuleContext failedRuleContext;
    private List<ParserRuleContext> failedRuleContexts = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        Token offendingToken = (Token) offendingSymbol;
        offendingTokenName = offendingToken.getText();
        TokenStream tokens = (TokenStream) recognizer.getInputStream();

        // 获取整个SQL语句的所有token
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() != Token.EOF) {
                String tokenName = recognizer.getVocabulary().getSymbolicName(token.getType());
//                System.out.println(tokenName);
                allTokens.add(tokenName);
            }
        }

        List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        String topRule = stack.get(stack.size() - 1); // 获取栈顶规则
        topRules.add(topRule);
        this.hasError = true;

        if (recognizer instanceof Parser) {
            Parser parser = (Parser) recognizer;
            ParserRuleContext failedRuleContext = parser.getContext();
            failedRuleContexts.add(failedRuleContext);
        }
    }

    public List<String> getAllTokens() {
        return allTokens;
    }

    public List<String> getTopRules() {
        return topRules;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getOffendingTokenName() {
        return offendingTokenName;
    }

    public List<ParserRuleContext> getFailedRuleContexts() {
        return failedRuleContexts;
    }
}

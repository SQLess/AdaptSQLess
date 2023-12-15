package org.antlr.v4;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/9/24 15:56
 */
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FailedRuleVisitor extends AbstractParseTreeVisitor<Void> {
    private List<String> failedRules;
    private Parser parser;
    private Map<String, List<String>> failedRuleChildrenMap = new HashMap<>();

    public FailedRuleVisitor(List<String> failedRules, Parser parser) {
        this.failedRules = failedRules;
        this.parser = parser;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        String ruleName = parser.getRuleNames()[node.getRuleContext().getRuleIndex()];
        if (failedRules.contains(ruleName)) {
            List<String> children = new ArrayList<>();
            int childCount = node.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ParseTree child = node.getChild(i);
                if (child instanceof TerminalNode) {
                    TerminalNode terminalNode = (TerminalNode) child;
                    String tokenTypeName = parser.getVocabulary().getSymbolicName(terminalNode.getSymbol().getType());
                    children.add(tokenTypeName);
                } else if (child instanceof ParserRuleContext) {
                    ParserRuleContext parserRuleContext = (ParserRuleContext) child;
                    String childRuleName = parser.getRuleNames()[parserRuleContext.getRuleIndex()];
                    children.add(childRuleName);
                }
            }
            failedRuleChildrenMap.put(ruleName, children);
        }
        return super.visitChildren(node);
    }

    public Map<String, List<String>> getFailedRuleChildrenMap() {
        return failedRuleChildrenMap;
    }
}

/**
 * @author lilin
 * @version 1.0
 * @date 2023/9/21 9:49
 */
package org.antlr.v4;

import org.antlr.v4.tool.ast.*;

import java.util.ArrayList;
import java.util.List;


public class GrammarASTVisitor {


    public List<Rule> generateGrammarAndRules(GrammarRootAST rootAST) {
        StringBuilder grammarContent = new StringBuilder();
        List<Rule> ruleList = new ArrayList<>();
        dfs(rootAST, grammarContent, ruleList);
        return ruleList;
    }


    /**
     * Pre-order traverse the Grammar Tree and return a StringBuilder(g4) and a pair<rule,definition>.
     * @param node
     * @param grammarContent
     */
    private void dfs(GrammarAST node, StringBuilder grammarContent,  List<Rule> ruleList) {
        if (node instanceof RuleAST) {
            RuleAST rule = (RuleAST) node;
            String ruleName = rule.getRuleName();
            List<?> children = node.getChildren();
            if (children != null) {
                for (Object child : children) {
                    if (child instanceof BlockAST && ((BlockAST) child).getChildren().size() > 1) {
                        for (Object altChild : ((BlockAST) child).getChildren()) {
                            String ruleDefinition = visitNode((GrammarAST) altChild, false).trim();
                            ruleDefinition = ruleDefinition.replaceAll("\\s+", " "); // Replace multiple spaces with a single space
                            grammarContent.append(ruleName + ": " + ruleDefinition + ";").append("\n");
                            ruleList.add(new Rule(ruleName, ruleDefinition));
                        }
                        return; // Avoid generating the rule again outside this block
                    }
                }
            }
            String ruleDefinition = visitNode(rule, false).trim();
            ruleDefinition = ruleDefinition.replaceAll("\\s+", " "); // Replace multiple spaces with a single space
            grammarContent.append(ruleName + ": " + ruleDefinition + ";").append("\n");
            ruleList.add(new Rule(ruleName, ruleDefinition));
        }
        if (node.getChildren() != null) {
            for (Object child : node.getChildren()) {
                dfs((GrammarAST) child, grammarContent,ruleList);
            }
        }
    }

    /**
     * While visiting Grammarast nodes, we perform some processing.
     * @param node
     * @param isAltSibling
     * @return
     */
    private String visitNode(GrammarAST node, boolean isAltSibling) {
        StringBuilder result = new StringBuilder();

        if (node instanceof TerminalAST) {
            result.append(node.getText() + " ");
        } else if (node instanceof RuleRefAST) {
            result.append(node.getText() + " ");
        } else if (node instanceof AltAST) {
            if (isAltSibling) {
                result.append("| ");
            }
            result.append(visitChildren(node, true));
        } else if (node instanceof OptionalBlockAST) {
            result.append("( " + visitChildren(node, false) + ")? ");
        } else if (node instanceof StarBlockAST) {
            result.append("( " + visitChildren(node, false) + ")* ");
        } else if (node instanceof PlusBlockAST){
            result.append("( " + visitChildren(node,false) + ")+");
        } else {
            result.append(visitChildren(node, false) + " ");
        }

        return result.toString();
    }

    private String visitChildren(GrammarAST node, boolean isInsideAlt) {
        StringBuilder result = new StringBuilder();
        List<?> children = node.getChildren();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                GrammarAST child = (GrammarAST) children.get(i);
                result.append(visitNode(child,  i > 0));
            }
        }
        return result.toString();
    }
}

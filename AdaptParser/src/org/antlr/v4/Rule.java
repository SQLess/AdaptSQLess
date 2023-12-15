package org.antlr.v4;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/9/21 16:05
 */

public class Rule {
    private String rule;
    private String definition;

    public Rule(String rule, String definition) {
        this.rule = rule;
        this.definition = definition;
    }

    public String getRule() {
        return rule;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return rule + ": " + definition + ";";
    }

    public static Rule Reduction(List<Rule> originalRules, Rule newRule) {
        String newDefinition = newRule.getDefinition();

        boolean replaced;
        do {
            replaced = false;
            Rule longestMatchRule = null;
            int longestMatchLength = 0;
            int longestMatchToken = 0;
            int longestMatchStart = 0;

            for (Rule originalRule : originalRules) {
//                Pattern pattern = Pattern.compile(Pattern.quote(originalRule.getDefinition()));
//                Pattern pattern = Pattern.compile("(?<=\\s|^)" + Pattern.quote(originalRule.getDefinition()) + "(?=\\s|$)");
                Pattern pattern = Pattern.compile("(?<=\\s|^)" + Pattern.quote(originalRule.getDefinition()) + "(?=\\s|$)");
                Matcher matcher = pattern.matcher(newDefinition);

                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();

                    if(newDefinition.substring(start,end).split(" ").length>longestMatchToken){
                        longestMatchToken = newDefinition.substring(start,end).split(" ").length;
                        longestMatchLength = end - start;
                        longestMatchRule = originalRule;
                        longestMatchStart = start;
                    }
                }
            }

//            System.out.println(longestMatchRule);
            if (longestMatchRule != null) {
                newDefinition = new StringBuilder(newDefinition)
                        .replace(longestMatchStart, longestMatchStart + longestMatchLength, longestMatchRule.getRule())
                        .toString();
                replaced = true;
//                System.out.println("Replacement: " + newDefinition);
            }
        } while (replaced);

        return new Rule(newRule.getRule(), newDefinition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule1 = (Rule) o;
        return Objects.equals(rule, rule1.rule) &&
                Objects.equals(definition, rule1.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, definition);
    }
}

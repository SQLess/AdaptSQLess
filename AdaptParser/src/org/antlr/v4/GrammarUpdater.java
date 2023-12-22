package org.antlr.v4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/9/25 20:25
 */
public class GrammarUpdater {
    /**
     * 将一条规则插入到g4文件中
     * @param filePath
     * @param newFilePath
     * @param ruleName
     * @param newRule
     */
    public static void updateGrammarFile(String filePath, String newFilePath, String ruleName, String newRule) {
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>();

            boolean ruleFound = false;
            for (String line : lines) {
                if (line.startsWith(ruleName + ":")) {
                    System.out.println(line);
                    ruleFound = true;
                }
                if (ruleFound && line.endsWith(";")) {
                    String updatedRule = line.substring(0, line.length() - 1) + " | " + newRule + " ;";
                    updatedLines.add(updatedRule);
                    ruleFound = false;
                } else {
                    updatedLines.add(line);
                }
            }

            Path newPath = Paths.get(newFilePath);
            Files.write(newPath, updatedLines);
            System.out.println("Grammar file updated successfully. New file: " + newFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to update the grammar file.");
        }
    }

    /**
     * 将一系列规则插入到g4文件中
     * @param filePath
     * @param newFilePath
     * @param rules
     */
    public static void updateGrammarFile(String filePath, String newFilePath, Set<Rule> rules) {
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>();

            // Create a map to store rule names and their corresponding definitions
            Map<String, List<String>> ruleDefinitionsMap = new HashMap<>();
            for (Rule rule : rules) {
                ruleDefinitionsMap.computeIfAbsent(rule.getRule(), k -> new ArrayList<>()).add(rule.getDefinition());
            }

            boolean ruleFound = false;
            String currentRuleName = null;
            List<String> currentNewRules = null;

            for (String line : lines) {
                if (ruleFound) {
                    // If a rule was found, check if the line ends with a semicolon
                    if (line.endsWith(";")) {
                        StringBuilder updatedRule = new StringBuilder(line.substring(0, line.length() - 1));
                        for (String newRule : currentNewRules) {
                            updatedRule.append(" | ").append(newRule).append("\n");
                        }
                        updatedRule.append(" ;");
                        updatedLines.add(updatedRule.toString());  // Update the line with new rules
//                        System.out.println(updatedRule);
                        ruleFound = false;  // Reset the flag as we've found the semicolon
                    } else {
                        updatedLines.add(line);  // Add the current line as is
                    }
                } else {
                    updatedLines.add(line);  // Add the current line as is
                    // Check if the line starts with any of the rule names
                    for (Map.Entry<String, List<String>> entry : ruleDefinitionsMap.entrySet()) {
                        String ruleName = entry.getKey();
                        if (line.startsWith(ruleName)) {
                            ruleFound = true;  // Set the flag to true as we've found a rule name
                            currentRuleName = ruleName;
                            currentNewRules = entry.getValue();
                            break;
                        }
                    }
                }
            }

            Path newPath = Paths.get(newFilePath);
            Files.write(newPath, updatedLines);
            System.out.println("Grammar file updated successfully. New file: " + newFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to update the grammar file.");
        }
    }

}

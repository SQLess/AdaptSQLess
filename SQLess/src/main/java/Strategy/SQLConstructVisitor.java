/**
 * @author LiLin
 * @version 1.0
 * @date 2023/10/27 下午2:15
 */
package Strategy;

import MySql.MySqlParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SQLConstructVisitor extends MySqlParserBaseVisitor<Void> {
    private StringBuilder builder = new StringBuilder();

    @Override
    public Void visitChildren(RuleNode node) {
        int count = node.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree child = node.getChild(i);
            child.accept(this);
        }
        return null; // 因为我们只关心用StringBuilder构建字符串，所以这里返回null
    }

    @Override
    public Void visitTerminal(TerminalNode node) {
        String text = node.getText();

        // 获取父节点
        ParseTree parent = node.getParent();
        if (parent != null) {
            int currentIndex = -1;
            int totalChildren = parent.getChildCount();

            // 找出当前节点在父节点中的索引
            for (int i = 0; i < totalChildren; i++) {
                if (parent.getChild(i) == node) {
                    currentIndex = i;
                    break;
                }
            }

            // 根据前一个和后一个兄弟节点的类型确定是否添加空格
            boolean addLeadingSpace = true;
            boolean addTrailingSpace = true;

            if (currentIndex > 0) {
                ParseTree prevSibling = parent.getChild(currentIndex - 1);
                // 如果前一个兄弟是某些特定类型的节点或文本，可能不需要前导空格
                addLeadingSpace = shouldAddSpaceBasedOnSibling(prevSibling, true);
            }

            if (currentIndex < totalChildren - 1) {
                ParseTree nextSibling = parent.getChild(currentIndex + 1);
                // 如果下一个兄弟是某些特定类型的节点或文本，可能不需要尾随空格
                addTrailingSpace = shouldAddSpaceBasedOnSibling(nextSibling, false);
            }

            // 根据需要添加空格
            if (addLeadingSpace && !builder.toString().endsWith(" ")) {
                builder.append(" ");
            }
            builder.append(text);
            if (addTrailingSpace) {
                builder.append(" ");
            }
        } else {
            // 如果没有足够的上下文信息，按照默认方式处理
            builder.append(text).append(" ");
        }
        return null;
    }

    /**
     * 是否需要添加空格，可以让还原出来的SQL语句更加美观
     * @param sibling
     * @param isPreviousSibling
     * @return
     */
    private boolean shouldAddSpaceBasedOnSibling(ParseTree sibling, boolean isPreviousSibling) {
        // 这里，您可能需要检查兄弟节点的类型或文本，以决定是否添加空格
        // 例如，如果兄弟节点是一个括号、逗号、操作符等，您可能会选择不添加空格
        // 您需要根据实际情况扩展这个方法
        String siblingText = sibling.getText();
        if (sibling instanceof TerminalNode) {
            if (isPreviousSibling) {
                // 检查前一个兄弟节点的文本，决定是否添加前导空格
                return !siblingText.matches(".*[.,;\\(]$");
            } else {
                // 检查后一个兄弟节点的文本，决定是否添加尾随空格
                return !siblingText.matches("^[.,;\\)]");
            }
        }
        // 对于非终端节点，您可能需要进行更复杂的检查
        return true;
    }



    public String getConstructedSQL() {
        return builder.toString().trim(); // 获取构建的字符串并删除最后的空格
    }

    public static String getSQLfromAST(RuleNode node){
        SQLConstructVisitor sqlConstructVisitor = new SQLConstructVisitor();
        sqlConstructVisitor.visit(node);
        String reconstructedSql = sqlConstructVisitor.getConstructedSQL();
        if (reconstructedSql.endsWith("<EOF>")) {
            reconstructedSql = reconstructedSql.replace("<EOF>", ";");
        }
        return reconstructedSql;
    }

}

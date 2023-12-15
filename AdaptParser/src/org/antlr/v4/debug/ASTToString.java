package org.antlr.v4.debug;

import org.antlr.v4.tool.ast.GrammarAST;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Convert ASTnode to a hierarchical string representation.
 */
public class ASTToString {

	public static String astToString(GrammarAST root) {
		StringBuilder result = new StringBuilder();
		bfsAstToString(root, result);
		return result.toString();
	}

	/**
	 * Level-order (BFS) traversal
	 * @param root
	 * @param result
	 */
	private static void bfsAstToString(GrammarAST root, StringBuilder result) {
		if (root == null) {
			return;
		}

		Queue<GrammarAST> queue = new LinkedList<>();
		queue.add(root);

		while (!queue.isEmpty()) {
			int size = queue.size();
			StringBuilder level = new StringBuilder();

			for (int i = 0; i < size; i++) {
				GrammarAST node = queue.poll();

				level.append(node.getText());

				for (int j = 0; j < node.getChildCount(); j++) {
					queue.add((GrammarAST) node.getChild(j));
				}

				if (i < size - 1) {
					level.append(", ");
				}
			}

			result.append(level).append("\n");
		}
	}

}

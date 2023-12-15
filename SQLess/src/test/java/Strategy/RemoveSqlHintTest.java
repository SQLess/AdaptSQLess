package Strategy;

import MySql.MySqlLexer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/31 15:13
 */
public class RemoveSqlHintTest {
    @Test
    public void TestRemoveSqlHint(){
        String originalSql = "select /*+ index(emp ind_emp_sal)*/ * from emp where deptno=200 and sal>300;";
        MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(originalSql));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        printAllTokens(tokens);
        String modifiedSql = RemoveSqlHint.removeOptimizationHints(tokens);
        System.out.println(modifiedSql);
    }
}

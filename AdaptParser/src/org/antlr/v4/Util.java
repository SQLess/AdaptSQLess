package org.antlr.v4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/9/22 12:37
 */
public class Util {
    public static List<String> getSqlsFromDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path");
            return new ArrayList<>();
        }

        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".sql"));
        if (files == null || files.length == 0) {
            System.out.println("No SQL files found in the directory");
            return new ArrayList<>();
        }

        List<String> allSqlStatements = new ArrayList<>();
        for (File file : files) {
            SqlFileParse sqlFileParse = new SqlFileParse(file.getAbsolutePath());
            sqlFileParse.parse();
            allSqlStatements.addAll(sqlFileParse.getResult());
        }

        return allSqlStatements;
    }

    public static List<String> getSqls(String path) throws IOException {
        SqlFileParse s = new SqlFileParse(path);
        s.parse();
        List<String> list = s.getResult();
        return list;
    }
}

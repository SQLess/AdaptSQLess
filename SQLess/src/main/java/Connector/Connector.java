package Connector;
/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/13 8:04
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connector {

    private String host;
    private int port;
    private String username;
    private String password;
    private String dbType;
    private String dbName;
    private Connection db;

    public Connector(String dbType,String host, int port, String username, String password,String dbName) throws SQLException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.dbType = dbType;
        this.dbName = dbName;

        String url = String.format("jdbc:%s://%s:%d/%s?allowOldPasswords=true", dbType, host, port,dbName);
        System.out.println(url);
        this.db = DriverManager.getConnection(url, username, password);

        if (!dbName.isEmpty()) {
            execSQL("CREATE DATABASE IF NOT EXISTS " + dbName);
            execSQL("USE " + dbName);
        }
    }

    public Result execSQL(String sql) {
        long startTime = System.currentTimeMillis();
        Statement stmt = null;
        Result result = new Result();

        try {
            stmt = db.createStatement();

            // 检查SQL语句类型，如果是数据操作语句，使用executeUpdate()，否则使用executeQuery()
            boolean isDataManipulation = isDataManipulationStatement(sql);
            if (isDataManipulation) {
                int rowsAffected = stmt.executeUpdate(sql);
                System.out.println(rowsAffected);
            } else {
                ResultSet rs = stmt.executeQuery(sql);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    result.getColumnNames().add(metaData.getColumnName(i));
                    result.getColumnTypes().add(metaData.getColumnTypeName(i));
                }

                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getString(i));
                    }
                    result.getRows().add(row);
                }
            }
        } catch (SQLException e) {
            result.setErr(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // Handle exception if needed
                }
            }
        }

        result.setTime(System.currentTimeMillis() - startTime);
        return result;
    }

    private boolean isDataManipulationStatement(String sql) {
        String trimmedSql = sql.trim().toUpperCase();
        return trimmedSql.startsWith("INSERT") || trimmedSql.startsWith("UPDATE") || trimmedSql.startsWith("DELETE") || trimmedSql.startsWith("CREATE");
    }

    public void initDB() {
        execSQL("DROP DATABASE IF EXISTS " + dbName);
        execSQL("CREATE DATABASE " + dbName);
        execSQL("USE " + dbName);
    }

    public void initDBWithDDL(List<String> ddlSqls) {
        initDB();
        for (String ddlSql : ddlSqls) {
            execSQL(ddlSql);
        }
    }

    public void initDBWithDDLPath(String ddlPath) {
        // TODO: Implement method to extract SQL from path and then call initDBWithDDL
    }

    public void close() {
        try {
            if (db != null) {
                db.close();
            }
        } catch (SQLException e) {
            // Handle exception if needed
            e.printStackTrace();
        }
    }
}

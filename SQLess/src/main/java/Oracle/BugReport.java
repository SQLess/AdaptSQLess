package Oracle;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/13 8:04
 */

//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
//
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// Other imports...
@JsonIgnoreProperties(ignoreUnknown = true)
public class BugReport {
    @JsonProperty("reportTime")
    private String reportTime;

    @JsonProperty("mutationName")
    private String mutationName;

    @JsonProperty("isUpper")
    private boolean isUpper;

    @JsonProperty("ddlPath")
    private String ddlPath;

    private String originalSql;

    private String mutatedSql;
    private String SlimoriginalSql;
    private String SlimmutatedSql;

    @JsonProperty("dbms")
    private String dbms;

    @JsonProperty("host")
    private String host;

    @JsonProperty("port")
    private int port;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("dbname")
    private String dbname;

    /**
     * Dataloader 初始化BugReport
     * @param json1Path
     * @param json2Path
     * @param logPath
     * @return
     * @throws IOException
     */
    public static BugReport DataLoader(String json1Path, String json2Path, String logPath,String ddlPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON files
        // No data seems to be taken from json1, so it's parsed but not used here.
        objectMapper.readValue(Files.readAllBytes(Paths.get(json1Path)), Object.class);
        BugReport json2Data = objectMapper.readValue(Files.readAllBytes(Paths.get(json2Path)), BugReport.class);

        // Read the log file and extract data (we're assuming a certain structure based on the provided log)
        List<String> logLines = Files.readAllLines(Paths.get(logPath), StandardCharsets.UTF_8);

        BugReport report = new BugReport();

        for (int i = 0; i < logLines.size(); i++) {
            String line = logLines.get(i);
            if (line.contains("-- OriginalSql")) {
                report.originalSql = logLines.get(i + 1).trim();
            } else if (line.contains("-- MutatedSql")) {
                report.mutatedSql = logLines.get(i + 1).trim();
            }
        }

        // Instantiate and return the BugReport object
        report.reportTime = json2Data.getReportTime();
        report.mutationName = "pinonlo";
        report.isUpper = json2Data.isUpper();
        report.ddlPath = ddlPath;
        report.dbms = "mysql";
//        report.dbms = "mariadb";
        report.host = "127.0.0.1";
        report.port = 13306;
//        report.port = 9101;
//        report.port = 2881;
//        report.port = 4000;
        report.username = "root";
        report.password = "123456";
        report.dbname = "TEST3";

        return report;
    }

    public void setPort(int port){
        this.port = port;
    }

    public String getReportTime() {
        return reportTime;
    }

    public String getMutationName() {
        return mutationName;
    }

    public boolean isUpper() {
        return isUpper;
    }

    public String getDdlPath() {
        return ddlPath;
    }

    public String getOriginalSql() {
        return originalSql;
    }

    public String getMutatedSql() {
        return mutatedSql;
    }

    public String getDbms() {
        return dbms;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDbname() {
        return dbname;
    }

    public String getSlimoriginalSql() {
        return SlimoriginalSql;
    }

    public void setSlimoriginalSql(String slimoriginalSql) {
        SlimoriginalSql = slimoriginalSql;
    }

    public String getSlimmutatedSql() {
        return SlimmutatedSql;
    }

    public void setSlimmutatedSql(String slimmutatedSql) {
        SlimmutatedSql = slimmutatedSql;
    }

    @Override
    public String toString() {
        return "BugReport{" +
                ", reportTime='" + reportTime + '\'' +
                ", mutationName='" + mutationName + '\'' +
                ", isUpper=" + isUpper +
                ", ddlPath='" + ddlPath + '\'' +
                ", originalSql='" + originalSql + '\'' +
                ", mutatedSql='" + mutatedSql + '\'' +
                ", dbms='" + dbms + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dbname='" + dbname + '\'' +
                '}';
    }

    public void writeToFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Serialize the current object to JSON and write to the specified file
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        Files.write(Paths.get(filePath), jsonString.getBytes(StandardCharsets.UTF_8));
    }

}


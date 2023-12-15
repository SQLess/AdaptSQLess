/**
 * @author LiLin
 * @version 1.0
 * @date 2023/11/2 下午7:58
 */
public class ArgumentReader {
    private String json1path;
    private String json2path;
    private String logpath;
    private String ddlpath;
    private String outputpath;

    public ArgumentReader(String[] args) {
        System.out.println(args.length);
        if (args.length != 5) {
            throw new IllegalArgumentException("Expected 5 arguments: json1path, json2path, logpath, ddlpath, outputpath");
        }

        this.json1path = args[0];
        this.json2path = args[1];
        this.logpath = args[2];
        this.ddlpath = args[3];
        this.outputpath = args[4];
    }

    public ArgumentReader(String json1Path, String json2Path, String logPath, String ddlPath, String outputpath) {
        this.json1path = json1Path;
        this.json2path = json2Path;
        this.logpath = logPath;
        this.ddlpath = ddlPath;
        this.outputpath = outputpath;

    }

    public String getJson1path() {
        return json1path;
    }

    public String getJson2path() {
        return json2path;
    }

    public String getLogpath() {
        return logpath;
    }

    public String getDdlpath() {
        return ddlpath;
    }

    @Override
    public String toString() {
        return "ArgumentReader{" +
                "json1path='" + json1path + '\'' +
                ", json2path='" + json2path + '\'' +
                ", logpath='" + logpath + '\'' +
                ", ddlpath='" + ddlpath + '\'' +
                ", outputpath='" + outputpath + '\'' +
                '}';
    }

    public String getOutputpath() {
        return outputpath;
    }
}


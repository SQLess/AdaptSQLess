/**
 * @author LiLin
 * @version 1.0
 * @date 2023/11/2 下午7:58
 */
public class ArgumentReader {
    private String initialPath;
    private String json1path;
    private String logpath;
    private String ddlpath;
    private String outputpath;

    public ArgumentReader(String[] args) {
        System.out.println(args.length);
        if (args.length != 5) {
            throw new IllegalArgumentException("Expected 5 arguments: json1path, json2path, logpath, ddlpath, outputpath");
        }
        this.initialPath = args[0];
        this.json1path = args[1];
        this.logpath = args[2];
        this.ddlpath = args[3];
        this.outputpath = args[4];
    }

    public ArgumentReader(String initialPath, String json1Path, String ddlPath, String outputpath) {
        this.initialPath = initialPath;
        this.json1path = json1Path;
//        this.logpath = logPath;
        this.ddlpath = ddlPath;
        this.outputpath = outputpath;
    }

    public String getInitialPath(){return initialPath;}
    public String getJson1path() {
        return json1path;
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
                ", logpath='" + logpath + '\'' +
                ", ddlpath='" + ddlpath + '\'' +
                ", outputpath='" + outputpath + '\'' +
                '}';
    }

    public String getOutputpath() {
        return outputpath;
    }
}


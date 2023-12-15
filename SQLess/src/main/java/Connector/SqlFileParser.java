package Connector;
/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/13 8:04
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlFileParser {
    protected String fileName;
    protected List<String> result;
    public SqlFileParser(String fileName){
        this.fileName= fileName;
        this.result=new ArrayList<>();
    }
    public void parse() throws IOException {
        StringBuilder stringBuilder=new StringBuilder();
        File f=new File(fileName);
        if (!f.exists()){
            System.out.println("no exitsts");
        }
        else {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line=br.readLine())!=null) {
                if (line.contains("#"))continue;
                if (line.contains("--"))  continue;
                if (line.strip().endsWith(";")){
                    stringBuilder.append(line);
                    result.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
                else {
                    stringBuilder.append(line);
                }

            }

        }
    }
    public List<String> getResult() {
        return result;
    }
}

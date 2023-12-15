package Oracle;

import java.util.ArrayList;
import java.util.List;

/**
 * (isUpper ^ flag) ^ 1
 */
public class Candidate {
    public static List<Candidate> candidates = new ArrayList<>();

    public static void clear() {
        candidates.clear();
    }

    public String mutateName;
    public int isUpper;
    public int flag;
    public String mutateSql;

    public Candidate(String mutateName, int isUpper, int flag, String mutateSql) {
        this.mutateName = mutateName;
        this.isUpper = isUpper;
        this.flag = flag;
        this.mutateSql = mutateSql;
    }
}

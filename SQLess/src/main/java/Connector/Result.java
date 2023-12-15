package Connector;
/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/13 8:04
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Result {
    private List<String> columnNames = new ArrayList<>();
    private List<String> columnTypes = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();
    private Exception err;
    private long time;

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getColumnTypes() {
        return columnTypes;
    }

    public void setColumnTypes(List<String> columnTypes) {
        this.columnTypes = columnTypes;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    public Exception getErr() {
        return err;
    }

    public void setErr(Exception err) {
        this.err = err;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ColumnNames(ColumnTypes): ");
        for (int i = 0; i < columnNames.size(); i++) {
            str.append(" ").append(columnNames.get(i)).append("(").append(columnTypes.get(i)).append(")");
        }
        str.append("\n");
        for (int i = 0; i < rows.size(); i++) {
            str.append("row ").append(i).append(":");
            List<String> row = rows.get(i);
            for (String data : row) {
                str.append(" ").append(data);
            }
            str.append("\n");
        }
        if (err != null) {
            str.append("Error: ").append(err.getMessage()).append("\n");
        }
        str.append(TimeUnit.MILLISECONDS.toMillis(time)).append(" ms");
        return str.toString();
    }

    /**
     * 这个方法将 rows 的每一行转化为一个逗号分隔的字符串，并返回这些字符串的列表。
     * @return
     */
    public List<String> flatRows() {
        List<String> flat = new ArrayList<>();
        for (List<String> r : rows) {
            StringBuilder t = new StringBuilder();
            for (int i = 0; i < r.size(); i++) {
                if (i != 0) {
                    t.append(",");
                }
                t.append(r.get(i));
            }
            flat.add(t.toString());
        }
        return flat;
    }

    public boolean isEmpty() {
        return columnNames.isEmpty();
    }

    /**
     * compare(Result another) : 这个方法用于比较两个 Result 对象：
     *     如果有错误，返回-2。
     *     如果两者都为空，返回0。
     *     如果当前对象为空，返回-1。
     *     如果另一个对象为空，返回1。
     *     如果列数不同，返回2。
     *     接着，方法检查两个结果中的数据行是否相等、一个是否包含另一个，或它们是否有差异，并返回相应的值。
     * @param another
     * @return
     */
    // Result.CMP:
    //   -1: another contains this
    //   0: eq
    //   1: this contains another
    //   2: others
    //   error: this.Err or another.Err
    //   do not consider the column name
    public int compare(Result another) {
        if (err != null || another.err != null) {
            return -2;
        }

        boolean empty1 = isEmpty();
        boolean empty2 = another.isEmpty();
        if (empty1 || empty2) {
            if (empty1 && empty2) {
                return 0;
            }
            if (empty1) {
                return -1;
            } else {
                return 1;
            }
        }

        if (columnNames.size() != another.columnNames.size()) {
            return 2;
        }

        List<String> res1 = flatRows();
        List<String> res2 = another.flatRows();

        Map<String, Integer> mp = new HashMap<>();
        for (int i = 0; i < res2.size(); i++) {
            String value = res2.get(i);
            mp.put(value, mp.getOrDefault(value, 0) + 1);
        }

        boolean allInAnother = true;
        for (int i = 0; i < res1.size(); i++) {
            String value = res1.get(i);
            if (mp.containsKey(value)) {
                int num = mp.get(value);
                if (num <= 1) {
                    mp.remove(value);
                } else {
                    mp.put(value, num - 1);
                }
            } else {
                allInAnother = false;
            }
        }

        if (allInAnother) {
            if (mp.isEmpty()) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (mp.isEmpty()) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    public void getResult(){
        List<String> columnNames = this.getColumnNames();
        List<List<String>> rows = this.getRows();

        // 输出列名
        for (String columnName : columnNames) {
            System.out.print(columnName + "\t");
        }
        System.out.println();

        // 输出查询结果
        for (List<String> row : rows) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

}

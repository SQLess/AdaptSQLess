package Strategy;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/24 15:16
 */

import java.util.Objects;
import java.util.UUID;

public class Node {
    public static final String USE_TABLE = "USE_TABLE";  // 表名
    public static final String USE_COLUMN = "USE_COLUMN";  // 列名
    public static final String QUERYEXPRESSION = "QUERYEXPRESSION";  // 子查询
    public static final String EXPRESSION = "USE_EXPRESSION";  // 表达式
    public static final String FUCTION = "FUNCTION";

    private final String id;
    private String alias;
    private final String name;
    private final String type;
    private int count;  // 用于存储use的个数

    public Node(String alias, String name, String type) {
        this.id = UUID.randomUUID().toString();
        this.alias = alias;
        this.name = name;
        this.type = type;
        this.count = 0;  // 初始化为0
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        this.count++;
    }

    public void initcount(){  //计数器初始化
        this.count = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name) && Objects.equals(type, node.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", alias='" + alias + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", count=" + count +
                '}';
    }
}

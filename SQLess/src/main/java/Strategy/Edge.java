package Strategy;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/24 15:17
 */
import java.util.UUID;

public class Edge {
//    public static final String DEF = "DEF";
//    public static final String USE = "USE";
    public static final String con = "CON";

    private final String id;
    private final String startNodeId;
    private final String endNodeId;
    private final String relationType;

    public Edge(String startNodeId, String endNodeId, String relationType) {
        this.id = UUID.randomUUID().toString();
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.relationType = relationType;
    }

    public Edge(String startNodeId, String endNodeId) {
        this.id = UUID.randomUUID().toString();
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.relationType = con;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getStartNodeId() {
        return startNodeId;
    }

    public String getEndNodeId() {
        return endNodeId;
    }

    public String getRelationType() {
        return relationType;
    }
}

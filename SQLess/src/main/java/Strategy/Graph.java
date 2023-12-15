package Strategy;

/**
 * @author lilin
 * @version 1.0
 * @date 2023/10/24 15:17
 */
import java.util.*;

public class Graph {
    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, List<Node>> adjList = new HashMap<>();
    private final Map<String, Node> aliasToNode = new HashMap<>();

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        aliasToNode.put(node.getAlias(),node);
        adjList.putIfAbsent(node.getId(), new ArrayList<>());
    }

    public Node getNodeByAlias(String alias) {
        if (aliasToNode.containsKey(alias)){
            return aliasToNode.get(alias);
        }else{
            return null;
        }
    }

    public void resetAllNodeCounts() {
        for (Node node : nodes.values()) {
            node.initcount();
        }
    }


    public void addEdge(Node from, Node to) {
        adjList.get(from.getId()).add(to);
    }

    public void printGraph() {
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

        // 添加所有起始节点到队列
        getStartNodes().forEach(queue::offer);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (!visited.contains(current.getId())) {
                visited.add(current.getId());
                System.out.println("Node: " + current); // 打印当前节点。这假设Node类有一个合理的toString方法。

                // 将所有邻接点添加到队列中
                List<Node> adjNodes = adjList.get(current.getId());
                if (adjNodes != null) {
                    for (Node adjacent : adjNodes) {
                        System.out.println("Edge: " + current + " --> " + adjacent); // 打印边的信息
                        if (!visited.contains(adjacent.getId())) {
                            queue.offer(adjacent);
                        }
                    }
                }
            }
        }
    }


    public void printNodeUseCounts() {
        for (Node node : nodes.values()) {
            System.out.println(node + " - Use Count: " + node.getCount());
        }
    }

    public void removeNode(Node node) {
        // 1. 从节点集合中删除该节点
        nodes.remove(node.getId());

        // 2. 从邻接列表中删除该节点及其所有的边
        adjList.remove(node.getId());

        // 3. 从其他节点的邻接列表中删除指向该节点的所有边
        for (List<Node> edges : adjList.values()) {
            edges.remove(node);
        }
    }


    // 获取没有入边的节点
    private List<Node> getStartNodes() {
        Set<String> allNodes = new HashSet<>(nodes.keySet());
        for (List<Node> edges : adjList.values()) {
            for (Node node : edges) {
                allNodes.remove(node.getId());
            }
        }
        List<Node> startNodes = new ArrayList<>();
        for (String nodeId : allNodes) {
            startNodes.add(nodes.get(nodeId));
        }
        return startNodes;
    }


    public Set<Node> reachableNodesFrom(Node startNode) {
        Set<Node> reachableNodes = new HashSet<>();
        dfsUtil(startNode, reachableNodes);
        return reachableNodes;
    }

    private void dfsUtil(Node node, Set<Node> visited) {
        // 标记节点为已访问
        visited.add(node);

        List<Node> neighbors = adjList.get(node.getId());
        if (neighbors != null) {
            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfsUtil(neighbor, visited); // 递归访问邻居
                }
            }
        }
    }

    /**
     * 获取count为1的结点，就是只有def,没有use
     * @return
     */
    public Set<String> getNodesWithCountOne() {
        Set<String> nodesWithCountOne = new HashSet<>();
        for (Node node : nodes.values()) {
            if (node.getCount() == 1) {
                nodesWithCountOne.add(node.getAlias());
            }
        }
        return nodesWithCountOne;
    }
}
///&& this.reachableNodesFrom(node)==null
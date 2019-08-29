import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.RailwaySystem;
import com.oocourse.specs3.models.PathNotFoundException;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;

import java.lang.Math;
import java.util.*;

public class MyRailwaySystem implements RailwaySystem {
    public static final int MAX_VALUE = 100000000;

    public static HashMap<Integer, Integer> unpleasantMap = new HashMap<>();

    private int pid = 0;
    private int vid = 0;
    private HashMap<Integer, Path> pathMap = new HashMap<>();
    private HashMap<Path, Integer> pidMap = new HashMap<>();
    private HashMap<Integer, Integer> nodeMap = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> adjMap = new HashMap<>();
    private HashMap<Pair, Integer> ShortestPath = new HashMap<>();
    private HashMap<Integer, Integer> spVisited = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> priceGraph = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> transferGraph = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> unpleasantGraph = new HashMap<>();
    private HashMap<Pair, Integer> leastPrice = new HashMap<>();
    private HashMap<Pair, Integer> leastTransfer = new HashMap<>();
    private HashMap<Pair, Integer> leastUnpleasant = new HashMap<>();

    public MyRailwaySystem() {}

    public int size() {
        return pathMap.size();
    }

    public boolean containsPath(Path path) {
        return pidMap.containsKey(path);
    }

    public boolean containsPathId(int pathId) {
        return pathMap.containsKey(pathId);
    }

    public Path getPathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        return pathMap.get(pathId);
    }

    public int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }
        return pidMap.get(path);
    }

    public int addPath(Path path) {
        if (path == null || !path.isValid()) {
            return 0;
        }
        if (pidMap.containsKey(path)) {
            return pidMap.get(path);
        }
        pathMap.put(++pid, path);
        pidMap.put(path, pid);

        int node1;
        int node2;
        for (int i = 0; i < path.size(); i++) {
            node1 = path.getNode(i);
            if (nodeMap.containsKey(node1)) {
                nodeMap.put(node1, nodeMap.get(node1) + 1);
            } else {
                nodeMap.put(node1, 1);
            }

            if (i < path.size() - 1) {
                node2 = path.getNode(i + 1);
                addAdjMap(node1, node2);
                addAdjMap(node2, node1);
            }
        }

        addOthers(path);

        clearCache();
        // showStatus();
        return pid;
    }

    private void addOthers(Path path) {
        HashMap<Integer, HashMap<Integer, Integer>> pathPriceGraph = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> pathTransferGraph = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> pathUnpleasantGraph = new HashMap<>();
        int node1;
        int node2;
        for (int i = 0; i < path.size() - 1; i++) {
            node1 = path.getNode(i);
            node2 = path.getNode(i + 1);
            addGraph(pathPriceGraph, node1, node2, 1);
            addGraph(pathPriceGraph, node2, node1, 1);
            addGraph(pathTransferGraph, node1, node2, 0);
            addGraph(pathTransferGraph, node2, node1, 0);
            addGraph(pathUnpleasantGraph, node1, node2, Math.max(unpleasantMap.get(node1), unpleasantMap.get(node2)));
            addGraph(pathUnpleasantGraph, node2, node1, Math.max(unpleasantMap.get(node1), unpleasantMap.get(node2)));
        }
        little2big(path, 2, pathPriceGraph, priceGraph);
        little2big(path, 1, pathTransferGraph, transferGraph);
        little2big(path, 32, pathUnpleasantGraph, unpleasantGraph);
    }

    private void little2big(Path path, int cost,
                            HashMap<Integer, HashMap<Integer, Integer>> little,
                            HashMap<Integer, HashMap<Integer, Integer>> big) {
        HashMap<Pair, Integer> map = new HashMap<>();
        for (int node : path) {
            dijkstra(node, little, map, true);
        }
        for (Map.Entry<Pair, Integer> entry : map.entrySet()) {
            Pair pair = entry.getKey();
            int weight = entry.getValue();
            addGraph(big, pair.getNode1(), pair.getNode2(), weight + cost);
            addGraph(big, pair.getNode2(), pair.getNode1(), weight + cost);
        }
    }

    private void addAdjMap(int node1, int node2) {
        HashMap<Integer, Integer> temp;
        if (adjMap.containsKey(node1)) {
            temp = adjMap.get(node1);
            if (temp.containsKey(node2)) {
                temp.put(node2, temp.get(node2) + 1);
            } else {
                temp.put(node2, 1);
            }
        } else {
            temp = new HashMap<>();
            temp.put(node2, 1);
            adjMap.put(node1, temp);
        }
    }

    private void addGraph(HashMap<Integer, HashMap<Integer, Integer>> graph, int node1, int node2, int weight) {
        HashMap<Integer, Integer> map;
        if (graph.containsKey(node1)) {
            map = graph.get(node1);
            if (!map.containsKey(node2) || map.get(node2) > weight) {
                map.put(node2, weight);
            }
        } else {
            map = new HashMap<>();
            map.put(node2, weight);
            graph.put(node1, map);
        }
    }

    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }
        int pathId = pidMap.remove(path);
        pathMap.remove(pathId);

        int node1;
        int node2;
        for (int i = 0; i < path.size(); i++) {
            node1 = path.getNode(i);
            int cnt = nodeMap.get(node1);
            if (cnt == 1) {
                nodeMap.remove(node1);
            } else {
                nodeMap.put(node1, cnt - 1);
            }

            if (i < path.size() - 1) {
                node2 = path.getNode(i + 1);
                removeAdjMap(node1, node2);
                removeAdjMap(node2, node1);
            }

        }

        clearGraph();
        for (Map.Entry<Integer, Path> entry : pathMap.entrySet()) {
            addOthers(entry.getValue());
        }

        clearCache();
        // showStatus();
        return pathId;
    }

    private void removeAdjMap(int node1, int node2) {
        HashMap<Integer, Integer> temp = adjMap.get(node1);
        int cnt = temp.get(node2);
        if (cnt == 1) {
            temp.remove(node2);
            if (temp.isEmpty()) {
                adjMap.remove(node1);
            }
        } else {
            temp.put(node2, cnt - 1);
        }
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        Path path = pathMap.get(pathId);
        try {
            removePath(path);
        } catch (PathNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getDistinctNodeCount() {
        return nodeMap.size();
    }

    public boolean containsNode(int nodeId) {
        return nodeMap.containsKey(nodeId);
    }

    public boolean containsEdge(int fromNodeId, int toNodeId) {
        if (!containsNode(fromNodeId) || !containsNode(toNodeId)) {
            return false;
        }
        return (adjMap.containsKey(fromNodeId)
                && adjMap.get(fromNodeId).containsKey(toNodeId));
    }

    public boolean isConnected(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException {
        if (!containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        }
        if (!containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        }
        if (fromNodeId == toNodeId) {
            return true;
        }
        if (containsEdge(fromNodeId, toNodeId)) {
            return true;
        }
        if (spVisited.containsKey(fromNodeId) && !spVisited.containsKey(toNodeId)
                || !spVisited.containsKey(fromNodeId)
                && spVisited.containsKey(toNodeId)) {
            return false;
        }
        if (spVisited.containsKey(fromNodeId) && spVisited.containsKey(toNodeId)) {
            return (spVisited.get(fromNodeId).equals(spVisited.get(toNodeId)));
        }
        return (bfs(fromNodeId, toNodeId) != -1);
    }

    public int getShortestPathLength(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        }
        if (fromNodeId == toNodeId) {
            return 0;
        }
        if (containsEdge(fromNodeId, toNodeId)) {
            return 1;
        }

        Pair pair = new Pair(fromNodeId, toNodeId);
        if (ShortestPath.containsKey(pair)) {
            return ShortestPath.get(pair);
        }
        return bfs(fromNodeId, toNodeId);
    }

    public int getLeastTicketPrice(int fromNodeId, int toNodeId) throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        }
        if (fromNodeId == toNodeId) {
            return 0;
        }
        if (containsEdge(fromNodeId, toNodeId)) {
            return 1;
        }

        Pair pair = new Pair(fromNodeId, toNodeId);
        if (leastPrice.containsKey(pair)) {
            return (leastPrice.get(pair) - 2);
        }

        dijkstra(fromNodeId, priceGraph, leastPrice, false);

        return (leastPrice.get(pair) - 2);
    }

    public int getLeastTransferCount(int fromNodeId, int toNodeId) throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        }
        if (fromNodeId == toNodeId || containsEdge(fromNodeId, toNodeId)) {
            return 0;
        }

        Pair pair = new Pair(fromNodeId, toNodeId);
        if (leastTransfer.containsKey(pair)) {
            return (leastTransfer.get(pair) - 1);
        }

        dijkstra(fromNodeId, transferGraph, leastTransfer, false);

        return (leastTransfer.get(pair) - 1);
    }

    public int getLeastUnpleasantValue(int fromNodeId, int toNodeId) throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!isConnected(fromNodeId, toNodeId)) {
            throw new NodeNotConnectedException(fromNodeId, toNodeId);
        }
        if (fromNodeId == toNodeId) {
            return 0;
        }
        if (containsEdge(fromNodeId, toNodeId)) {
            return Math.max(unpleasantMap.get(fromNodeId), unpleasantMap.get(toNodeId));
        }

        Pair pair = new Pair(fromNodeId, toNodeId);
        if (leastUnpleasant.containsKey(pair)) {
            return (leastUnpleasant.get(pair) - 32);
        }

        dijkstra(fromNodeId, unpleasantGraph, leastUnpleasant, false);

        return (leastUnpleasant.get(pair) - 32);
    }

    public int getConnectedBlockCount() {
        int node;
        for (Map.Entry<Integer, Integer> entry : nodeMap.entrySet()) {
            node = entry.getKey();
            if (!spVisited.containsKey(node)) {
                bfs(node, node);
            }
        }
        return vid;
    }

    public int getUnpleasantValue(Path path, int fromIndex, int toIndex) {
        return 0;
    }


    private int bfs(int fromNodeId, int toNodeId) {
        int node1;
        int node2;
        int weight1;
        int weight2;
        int result = -1;
        WeightedNode wn;

        if (!spVisited.containsKey(fromNodeId)) {
            vid++;
        }
        HashMap<Integer, Integer> temp;
        HashMap<Integer, Integer> tempVisited = new HashMap<>();
        LinkedList<WeightedNode> queue = new LinkedList<>();
        queue.offer(new WeightedNode(fromNodeId, 0));
        spVisited.put(fromNodeId, vid);
        tempVisited.put(fromNodeId, vid);
        while (!queue.isEmpty()) {
            wn = queue.poll();
            node1 = wn.getNode();
            weight1 = wn.getWeight();
            weight2 = weight1 + 1;
            temp = adjMap.get(node1);
            for (Map.Entry<Integer, Integer> entry : temp.entrySet()) {
                node2 = entry.getKey();
                if (!tempVisited.containsKey(node2)) {
                    ShortestPath.put(new Pair(fromNodeId, node2), weight2);
                    if (node2 == toNodeId) {
                        result = weight2;
                    }
                    queue.offer(new WeightedNode(node2, weight2));
                    spVisited.put(node2, vid);
                    tempVisited.put(node2, vid);
                }
            }
        }

        return result;
    }

    private void dijkstra(int start, HashMap<Integer, HashMap<Integer, Integer>> graph,
                          HashMap<Pair, Integer> leastMap, boolean connected) {
        HashMap<Integer, Integer> disMap = new HashMap<>();
        if (connected) {
            for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : graph.entrySet()) {
                int node = entry.getKey();
                if (graph.get(start).containsKey(node)) {
                    disMap.put(node, graph.get(start).get(node));
                } else {
                    disMap.put(node, MAX_VALUE);
                }
            }
        } else {
            int vid = spVisited.get(start);
            for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : graph.entrySet()) {
                int node = entry.getKey();
                if (spVisited.containsKey(node) && spVisited.get(node) == vid) {
                    if (graph.get(start).containsKey(node)) {
                        disMap.put(node, graph.get(start).get(node));
                    } else {
                        disMap.put(node, MAX_VALUE);
                    }
                }
            }
        }
        disMap.put(start, -1);

        int node1 = -9999999;
        int node2;
        int n = disMap.size();
        for (int i = 1; i < n; i++) {
            int d;
            int dmin = MAX_VALUE;
            for (Map.Entry<Integer, Integer> entry : disMap.entrySet()) {
                d = entry.getValue();
                if (d != -1 && d < dmin) {
                    dmin = d;
                    node1 = entry.getKey();
                }
            }
            Pair pair = new Pair(start, node1);
            if (!leastMap.containsKey(pair) || leastMap.get(pair) > dmin) {
                leastMap.put(pair, dmin);
            }
            disMap.put(node1, -1);

            for (Map.Entry<Integer, Integer> entry : disMap.entrySet()) {
                if (entry.getValue() == -1) continue;
                node2 = entry.getKey();
                if (graph.get(node1).containsKey(node2)) {
                    int weight = graph.get(node1).get(node2);
                    int dnew = dmin + weight;
                    if (dnew < disMap.get(node2)) {
                        disMap.put(node2, dnew);
                    }
                }
            }
        }

    }

    private void clearCache() {
        ShortestPath.clear();
        spVisited.clear();
        leastPrice.clear();
        leastTransfer.clear();
        leastUnpleasant.clear();
        vid = 0;
    }

    private void clearGraph() {
        priceGraph.clear();
        transferGraph.clear();
        unpleasantGraph.clear();
    }

    private void showStatus() {
        System.out.println("pathMap:");
        for (Map.Entry<Integer, Path> entry : pathMap.entrySet()) {
            System.out.println("pathId: " + entry.getKey() + " path: "
                    + entry.getValue());
        }
        System.out.println();

        System.out.println("nodeMap:");
        for (Map.Entry<Integer, Integer> entry : nodeMap.entrySet()) {
            System.out.println("node: " + entry.getKey() + " num: "
                    + entry.getValue());
        }
        System.out.println();

        System.out.println("adjMap:");
        HashMap<Integer, Integer> temp;
        for (Map.Entry<Integer, HashMap<Integer, Integer>> entry
                : adjMap.entrySet()) {
            System.out.println("node: " + entry.getKey()
                    + " is connected with");
            temp = entry.getValue();
            for (Map.Entry<Integer, Integer> entry1 : temp.entrySet()) {
                System.out.println("node: " + entry1.getKey() + " num: "
                        + entry1.getValue());
            }
        }
        System.out.println();

        System.out.println("priceGraph:");
        HashMap<Integer, Integer> temp1;
        for (Map.Entry<Integer, HashMap<Integer, Integer>> entry
                : priceGraph.entrySet()) {
            System.out.println("node: " + entry.getKey()
                    + " is connected with");
            temp1 = entry.getValue();
            for (Map.Entry<Integer, Integer> entry1 : temp1.entrySet()) {
                System.out.println("node: " + entry1.getKey() + " weight: "
                        + entry1.getValue());
            }
        }
        System.out.println();
    }
}


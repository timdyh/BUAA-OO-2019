import com.oocourse.specs2.models.Path;
import com.oocourse.specs2.models.Graph;
import com.oocourse.specs2.models.PathNotFoundException;
import com.oocourse.specs2.models.PathIdNotFoundException;
import com.oocourse.specs2.models.NodeIdNotFoundException;
import com.oocourse.specs2.models.NodeNotConnectedException;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

public class MyGraph implements Graph {
    private int pid;
    private int vid;
    private HashMap<Integer, Path> pathMap;
    private HashMap<Path, Integer> pidMap;
    private HashMap<Integer, Integer> nodeMap;
    private HashMap<Integer, HashMap<Integer, Integer>> adjMap;
    private HashMap<ShortestPath, Integer> spMap;
    private HashMap<Integer, Integer> visited;

    public MyGraph() {
        pid = 0;
        vid = 0;
        pathMap = new HashMap<>();
        pidMap = new HashMap<>();
        nodeMap = new HashMap<>();
        adjMap = new HashMap<>();
        spMap = new HashMap<>();
        visited = new HashMap<>();
    }

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
        addNodeAndEdge(path);
        clear();

        return pid;
    }

    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }
        int pathId = pidMap.remove(path);
        pathMap.remove(pathId);
        removeNodeAndEdge(path);
        clear();

        return pathId;
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        Path path = pathMap.remove(pathId);
        pidMap.remove(path);
        removeNodeAndEdge(path);
        clear();
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
        if (visited.containsKey(fromNodeId) && !visited.containsKey(toNodeId)
                || !visited.containsKey(fromNodeId)
                && visited.containsKey(toNodeId)) {
            return false;
        }
        if (visited.containsKey(fromNodeId) && visited.containsKey(toNodeId)) {
            return (visited.get(fromNodeId).equals(visited.get(toNodeId)));
        }
        return (calculate(fromNodeId, toNodeId) != -1);
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

        ShortestPath sp = new ShortestPath(fromNodeId, toNodeId);
        if (spMap.containsKey(sp)) {
            return spMap.get(sp);
        }
        return calculate(fromNodeId, toNodeId);
    }

    private void addNodeAndEdge(Path path) {
        int node1;
        int node2;
        HashMap<Integer, Integer> temp;
        for (int i = 0; i < path.size(); i++) {
            node1 = path.getNode(i);
            if (nodeMap.containsKey(node1)) {
                nodeMap.put(node1, nodeMap.get(node1) + 1);
            } else {
                nodeMap.put(node1, 1);
            }

            if (i < path.size() - 1) {
                node2 = path.getNode(i + 1);
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

                node2 = path.getNode(i);
                node1 = path.getNode(i + 1);
                if (adjMap.containsKey(node1)) {
                    temp = adjMap.get(node1);
                    if (temp.containsKey(node2)) {
                        temp.put(node2, temp.get(node2) + 1);
                    } else {
                        temp.put(node2, 1);
                    }
                } else {	
                    temp.put(node2, 1);
                    adjMap.put(node1, temp);
                }
            }
        }
    }

    private void removeNodeAndEdge(Path path) {
        int node1;
        int node2;
        HashMap<Integer, Integer> temp;
        int cnt;
        for (int i = 0; i < path.size(); i++) {
            node1 = path.getNode(i);
            cnt = nodeMap.get(node1);
            if (cnt == 1) {
                nodeMap.remove(node1);
            } else {
                nodeMap.put(node1, cnt - 1);
            }

            if (i < path.size() - 1) {
                node2 = path.getNode(i + 1);
                temp = adjMap.get(node1);
                cnt = temp.get(node2);
                if (cnt == 1) {
                    temp.remove(node2);
                    if (temp.isEmpty()) {
                        adjMap.remove(node1);
                    }
                } else {
                    temp.put(node2, cnt - 1);
                }

                node2 = path.getNode(i);
                node1 = path.getNode(i + 1);
                temp = adjMap.get(node1);
                cnt = temp.get(node2);
                if (cnt == 1) {
                    temp.remove(node2);
                    if (temp.isEmpty()) {
                        adjMap.remove(node1);
                    }
                } else {
                    temp.put(node2, cnt - 1);
                }
            }
        }
    }

    private int calculate(int fromNodeId, int toNodeId) {
        int node1;
        int node2;
        int weight1;
        int weight2;
        int result = -1;
        WeightedNode wn;

        vid++;
        HashMap<Integer, Integer> temp;
        HashMap<Integer, Integer> tempVisited = new HashMap<>();
        LinkedList<WeightedNode> queue = new LinkedList<>();
        queue.offer(new WeightedNode(fromNodeId, 0));
        visited.put(fromNodeId, vid);
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
                    spMap.put(new ShortestPath(fromNodeId, node2), weight2);
                    if (node2 == toNodeId) {
                        result = weight2;
                    }
                    queue.offer(new WeightedNode(node2, weight2));
                    visited.put(node2, vid);
                    tempVisited.put(node2, vid);
                }
            }
        }

        return result;
    }

    private void clear() {
        spMap.clear();
        visited.clear();
        vid = 0;
    }
}


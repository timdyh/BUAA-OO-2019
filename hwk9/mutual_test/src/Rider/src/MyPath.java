import com.oocourse.specs1.models.Path;

import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

public class MyPath implements Path {
    private ArrayList<Integer> nodeList = new ArrayList<>();
    private HashMap<Integer, Integer> nodeMap = new HashMap<>();
    private StringBuilder nodeStr = new StringBuilder();
    private HashSet<Integer> nodeSet = new HashSet<>();

    public MyPath(int[] ppNodeList) {
        for (int i = 0; i < ppNodeList.length; i++) {
            nodeList.add(ppNodeList[i]);
            nodeStr.append(ppNodeList[i]);
            nodeSet.add(ppNodeList[i]);
            if (nodeMap.containsKey(ppNodeList[i])) {
                nodeMap.put(ppNodeList[i], nodeMap.get(ppNodeList[i]) + 1);
            } else {
                nodeMap.put(ppNodeList[i], 1);
            }
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return nodeList.iterator();
    }

    @Override
    public int compareTo(Path ppPath) {
        int len = Integer.min(this.size(), ppPath.size());
        int i;
        int sign = 0;
        for (i = 0; i < len; i++) {
            if (this.getNode(i) < ppPath.getNode(i)) {
                sign = -1;
                break;
            } else if (this.getNode(i) > ppPath.getNode(i)) {
                sign = 1;
                break;
            }
        }
        if (i == len) {
            return (this.size() - ppPath.size());
        }
        return sign;
    }

    public int size() {
        return nodeList.size();
    }

    public int getNode(int index) {
        return nodeList.get(index);
    }

    public boolean containsNode(int node) {
        return nodeSet.contains(node);
    }

    public int getDistinctNodeCount() {
        return nodeSet.size();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Path)) {
            return false;
        }
        Path toComp = (Path) obj;
        return this.toString().equals(toComp.toString());
    }

    public boolean isValid() {
        return this.nodeList.size() >= 2;
    }

    @Override
    public String toString() {
        return nodeStr.toString();
    }

    public HashSet<Integer> getDistinctNode() {
        return nodeSet;
    }
}

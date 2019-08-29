import com.oocourse.specs3.models.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.lang.Math;

public class MyPath implements Path {
    private final int distinctNodeCount;
    private final int hashcode;

    private ArrayList<Integer> nodeList;
    private HashMap<Integer, Integer> nodeMap;

    public MyPath(int... nodeList) {
        this.nodeList = new ArrayList<>();
        this.nodeMap = new HashMap<>();
        int node;
        int unpleasantValue;
        int hash = 0;
        for (int i = 0; i < nodeList.length; i++) {
            node = nodeList[i];
            this.nodeList.add(node);
            this.nodeMap.put(node, 1);
            hash += node * (i + 1);
            if (!MyRailwaySystem.unpleasantMap.containsKey(node)) {
                unpleasantValue = (int) (Math.pow(4, (node % 5 + 5) % 5));
                MyRailwaySystem.unpleasantMap.put(node, unpleasantValue);
            }
        }
        hashcode = hash;
        distinctNodeCount = new HashSet<>(this.nodeList).size();
    }

    public int size() {
        return nodeList.size();
    }

    public int getNode(int index) {
        return nodeList.get(index);
    }

    public boolean containsNode(int node) {
        return nodeMap.containsKey(node);
    }

    public int getDistinctNodeCount() {
        return distinctNodeCount;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Path) {
            if (((Path) obj).size() != this.size()) {
                return false;
            }
            for (int i = 0; i < this.size(); i++) {
                if (((Path) obj).getNode(i) != this.getNode(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isValid() {
        return nodeList.size() >= 2;
    }

    public int getUnpleasantValue(int nodeId) {
        return MyRailwaySystem.unpleasantMap.get(nodeId);
    }

    @Override
    public Iterator<Integer> iterator() {
        return nodeList.iterator();
    }

    @Override
    public int compareTo(Path o) {
        int length1 = this.size();
        int length2 = o.size();

        for (int i = 0; i < length1 && i < length2; i++) {
            if (this.getNode(i) < o.getNode(i)) {
                return -1;
            } else if (this.getNode(i) > o.getNode(i)) {
                return 1;
            }
        }
        return Integer.compare(length1, length2);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public String toString() {
        return nodeList.toString();
    }
}
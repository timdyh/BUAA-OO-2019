import com.oocourse.specs2.models.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MyPath implements Path {
    private final int distinctNodeCount;
    private final int hashcode;

    private ArrayList<Integer> nodeList;
    private HashMap<Integer, Integer> nodeMap;

    public MyPath(int... nodeList) {
        this.nodeList = new ArrayList<>();
        this.nodeMap = new HashMap<>();
        for (Integer node : nodeList) {
            this.nodeList.add(node);
            this.nodeMap.put(node, 1);
        }

        distinctNodeCount = new HashSet<>(this.nodeList).size();

        int hash = 0;
        for (int i = 0; i < this.nodeList.size(); i++) {
            hash += this.nodeList.get(i) * (i + 1);
        }
        hashcode = hash;
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
}
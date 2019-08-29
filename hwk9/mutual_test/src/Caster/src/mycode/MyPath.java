package mycode;

import com.oocourse.specs1.models.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MyPath implements Path {
    private ArrayList<Integer> nodeArr;
    private HashSet<Integer> nodeSet;
    private int hashCode;

    public MyPath(int[] nodeList) {
        nodeArr = new ArrayList<Integer>();
        nodeSet = new HashSet<Integer>();
        hashCode = 0;
        for (int i = 0; i < nodeList.length; i++) {
            nodeArr.add(nodeList[i]);
            nodeSet.add(nodeList[i]);
            hashCode = hashCode * 31 + nodeList[i];
        }
    }

    public /*@pure@*/int size() {
        return nodeArr.size();
    }

    public /*@pure@*/ int getNode(int index) {
        if (index >= 0 && index < size()) {
            return nodeArr.get(index);
        }
        return 0;
    }

    public /*@pure@*/ boolean containsNode(int node) {
        return nodeSet.contains(node);
    }

    public /*pure*/ int getDistinctNodeCount() {
        return nodeSet.size();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Path &&
                    ((Path) obj).size() == size()) {
            for (int i = 0; i < size(); i++) {
                if (((Path) obj).getNode(i) != getNode(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public /*@pure@*/ boolean isValid() {
        return nodeArr.size() >= 2;
    }

    @Override
    public Iterator<Integer> iterator() {
        return nodeArr.iterator();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public int compareTo(Path o) {
        int compareLen = Math.min(size(), o.size());
        for (int i = 0; i < compareLen; i++) {
            if (getNode(i) < o.getNode(i)) {
                return -1;
            } else if (getNode(i) > o.getNode(i)) {
                return 1;
            } else {
                continue;
            }
        }
        if (size() == o.size()) {
            return 0;
        } else if (size() > o.size()) {
            return 1;
        } else {
            return -1;
        }
    }
}

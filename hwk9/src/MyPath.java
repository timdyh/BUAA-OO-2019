import com.oocourse.specs1.models.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MyPath implements Path {
    // Iterable<Integer>和Comparable<Path>接口的规格请参阅JDK
    //@ public instance model non_null int[] nodes;

    private /*@spec_public@*/ final int distinctNodeCount;
    private /*@spec_public@*/ final int hashcode;

    private /*@spec_public@*/ ArrayList<Integer> nodeList;
    private /*@spec_public@*/ HashMap<Integer, Integer> nodeMap;

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

    //@ ensures \result == nodes.length;
    public /*@pure@*/ int size() {
        return nodeList.size();
    }

    /*@ requires index >= 0 && index < size();
      @ assignable \nothing;
      @ ensures \result == nodes[index];
      @*/
    public /*@pure@*/ int getNode(int index) {
        return nodeList.get(index);
    }

    /*@ ensures \result == (\exists int i; 0 <= i && i < nodes.length;
                            nodes[i] == node);
      @*/
    public /*@pure@*/ boolean containsNode(int node) {
        return nodeMap.containsKey(node);
    }

    /*@ ensures \result == (\num_of int i, j; 0 <= i && i < j
                            && j < nodes.length; nodes[i] != nodes[j]);
      @*/
    public /*pure*/ int getDistinctNodeCount() {
        return distinctNodeCount;
    }

    /*@ also
      @ public normal_behavior
      @ requires obj != null && obj instanceof Path;
      @ assignable \nothing;
      @ ensures \result == ((Path) obj).nodes.length == nodes.length) &&
      @                      (\forall int i; 0 <= i && i < nodes.length;
                             nodes[i] == ((Path) obj).nodes[i]);
      @ also
      @ public normal_behavior
      @ requires obj == null || !(obj instanceof Path);
      @ assignable \nothing;
      @ ensures \result == false;
      @*/
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Path) {
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

    //@ ensures \result == (nodes.length >= 2);
    public /*@pure@*/ boolean isValid() {
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
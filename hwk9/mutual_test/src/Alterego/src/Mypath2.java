import com.oocourse.specs1.models.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Mypath2 implements Path {
    // Iterable<Integer>和Comparable<Path>接口的规格请参阅JDK
    //@ public instance model non_null int[] nodes;
    private ArrayList<Integer> nodeList;

    public Mypath2(int... nodeList) {
        ArrayList<Integer> node = new ArrayList<>();
        for (Integer i : nodeList) {
            node.add(i);
        }
        this.nodeList = node;
    }

    //@ ensures \result == nodes.length;
    public /*@pure@*/int size() {
        return nodeList.size();
    }

    /*@ requires index >= 0 && index < size();
      @ assignable \nothing;
      @ ensures \result == nodes[index];
      @*/
    public /*@pure@*/ int getNode(int index) {
        assert (index >= 0 && index < nodeList.size());
        return nodeList.get(index);

    }

    //@ ensures \result == (\exists int i; 0 <= i
    // && i < nodes.length; nodes[i] == node);
    public /*@pure@*/ boolean containsNode(int node) {
        int i = 0;
        for (i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i) == node) {
                return true;
            }
        }
        return false;
    }

    ;

    /*@ ensures \result == (\num_of int i, j; 0 <= i
    && i < j && j < nodes.length;
                             nodes[i] != nodes[j]);
      @*/
    public /*pure*/ int getDistinctNodeCount() {
        HashSet<Integer> a = new HashSet<>();
        int i = 0;
        for (i = 0; i < nodeList.size(); i++) {
            a.add(nodeList.get(i));
        }
        return a.size();
    }

    /*@ also
      @ public normal_behavior
      @ requires obj != null && obj instanceof Path;
      @ assignable \nothing;
      @ ensures \result == ((Path) obj).nodes.length == nodes.length) &&
      @                      (\forall int i; 0 <= i &&
      i < nodes.length; nodes[i] == ((Path) obj).nodes[i]);
      @ also
      @ public normal_behavior
      @ requires obj == null || !(obj instanceof Path);
      @ assignable \nothing;
      @ ensures \result == false;
      @*/
    public boolean equals(Object obj) {
        /*
        Path x = (Path) obj;
        if (x.size() != nodeList.size()) {
            return false;
        } else {
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i) != x.getNode(i)) {
                    return false;
                }
            }
        }
        return true;
        */
        if (this == obj) {
            return true;
        }
        if (obj instanceof Path && obj != null) {
            Path x = (Path) obj;
            if (x.size() == this.size()) {
                int i = 0;
                int n = x.size();
                while (n-- != 0) {
                    if (x.getNode(i) != this.getNode(i)) {
                        return false;
                    }
                    i++;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        // TODO Auto-generated method stub
        //this.nodeList.hashCode();
        /*int hash = 0;
        int i = 0;
        for (i = 0; i < this.size(); i++) {
            hash += 7 * hash + nodeList.get(i);
        }
        return hash;*/
        return this.nodeList.hashCode();
    }

    //@ ensures \result == (nodes.length >= 2);
    public /*@pure@*/ boolean isValid() {
        if (nodeList.size() >= 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Path o) {
        assert (this.nodeList != null && o != null);
        int i = 0;
        for (i = 0; i < Integer.min(this.size(), o.size()); i++) {
            if (this.getNode(i) != o.getNode(i)) {
                return Integer.compare(this.getNode(i), o.getNode(i));
            }
        }
        return Integer.compare(this.size(), o.size());
    }

    @Override
    public Iterator<Integer> iterator() {
        return nodeList.iterator();
    }
}

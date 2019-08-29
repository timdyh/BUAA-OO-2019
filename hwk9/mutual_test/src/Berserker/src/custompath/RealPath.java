package custompath;

import com.oocourse.specs1.models.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class RealPath implements Path {
    private ArrayList<Integer> nodes;
    private HashSet<Integer> diffNodes;
    private int sum;
    private int code;

    public RealPath(int... nodeList) {
        nodes = new ArrayList<>(nodeList.length);
        for (int i : nodeList) {
            nodes.add(i);
        }
        diffNodes = new HashSet<>(nodes);
        sum = diffNodes.size();
        code = nodes.hashCode();
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public int getNode(int i) {
        return nodes.get(i);
    }

    @Override
    public boolean containsNode(int i) {
        return diffNodes.contains(i);
    }

    @Override
    public int getDistinctNodeCount() {
        return sum;
    }

    @Override
    public boolean isValid() {
        return nodes.size() >= 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (!(obj instanceof Path)) { return false; }
        Path tmp = (Path) obj;
        if (size() != tmp.size()) { return false; }
        return this.compareTo(tmp) == 0;
    }

    public int hashCode() {
        return code;
    }

    @Override
    public int compareTo(Path o) {
        Iterator<Integer> now = this.iterator();
        Iterator<Integer> ano = o.iterator();
        while (now.hasNext() && ano.hasNext()) {
            int r = now.next().compareTo(ano.next());
            if (r != 0) { return r; }
        }
        return Integer.compare(size(),o.size());
    }

    @Override
    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }
}

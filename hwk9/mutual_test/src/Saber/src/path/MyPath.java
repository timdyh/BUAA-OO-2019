package path;

import com.oocourse.specs1.models.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MyPath implements Path {
    private List<Integer> nodes;
    private int distinctSize;

    public MyPath(ArrayList<Integer> nodes) {
        this.nodes = nodes;
    }

    public MyPath(int... nodes) {
        this.nodes = new ArrayList<>();

        Arrays
            .stream(nodes)
            .forEach(this.nodes::add);

        distinctSize = (int)this.nodes.stream().distinct().count();
    }

    public int size() {
        return nodes.size();
    }

    public int getNode(int index) {
        return nodes.get(index);
    }

    public boolean containsNode(int node) {
        return nodes.contains(node);
    }

    public int getDistinctNodeCount() {
        return distinctSize;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Path)) {
            return false;
        }

        return obj == this || compareTo((Path) obj) == 0;
    }

    @Override
    public int hashCode() {
        return nodes.hashCode();
    }

    public boolean isValid() {
        return this.size() >= 2 && this.size() <= 1000;
    }

    @Override
    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }

    @Override
    public int compareTo(Path path) {
        for (int i = 0; i < this.size() && i < path.size(); i++) {
            if (this.getNode(i) > path.getNode(i)) {
                return 1;
            }
            if (this.getNode(i) < path.getNode(i)) {
                return -1;
            }
        }
        return Integer.compare(this.size(), path.size());
    }
}

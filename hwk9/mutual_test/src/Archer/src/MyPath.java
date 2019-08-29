import com.oocourse.specs1.models.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyPath implements Path {
    private ArrayList<Integer> nodes;
    private HashMap hashMap = new HashMap();

    public MyPath(int[] nodeList) {
        nodes = new ArrayList<Integer>();
        int temp = nodeList.length;
        for (int i = 0;i < temp; i += 1) {
            nodes.add(nodeList[i]);
            hashMap.put(nodeList[i],nodeList[i]);
        }
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
        return hashMap.size();
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            if (!MyPath.class.isInstance(obj)) {
                return false;
            }
            else {
                Path temp = Path.class.cast(obj);
                if (temp.size() == nodes.size()) {
                    return compareTo(temp) == 0;
                }
                else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }

    public boolean isValid() {
        return (size() >= 2);
    }

    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }

    public int compareTo(Path o) {
        int i = 0;
        for (Integer integer : o) {
            if (i >= nodes.size()) {
                return -1;
            }
            if (nodes.get(i) < integer) {
                return -1;
            }
            if (nodes.get(i) > integer) {
                return 1;
            }
            i += 1;
        }
        if (i == nodes.size()) {
            return 0;
        }
        return 1;
    }

}

import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.HashMap;
import java.util.HashSet;

public class MyPathContainer implements PathContainer {
    private int curId = 0;
    private HashMap<Integer, Path> idMap = new HashMap<>();
    private HashMap<String, Integer> pathMap = new HashMap<>();

    private HashMap<Integer, Integer> distinctNode = new HashMap<>();

    public MyPathContainer() {

    }

    public int size() {
        return idMap.size();
    }

    public boolean containsPath(Path path) {
        return pathMap.containsKey(path.toString());
    }

    public boolean containsPathId(int pathId) {
        return idMap.containsKey(pathId);
    }

    public Path getPathById(int pathId) throws PathIdNotFoundException {
        if (idMap.containsKey(pathId)) {
            return idMap.get(pathId);
        }
        throw new PathIdNotFoundException(pathId);
    }

    public int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid()
                || !pathMap.containsKey(path.toString())) {
            throw new PathNotFoundException(path);
        }
        return pathMap.get(path.toString());
    }

    public int addPath(Path path) {
        if (path == null || !path.isValid()) {
            return 0;
        }
        if (pathMap.containsKey(path.toString())) {
            return pathMap.get(path.toString());
        }
        curId++;
        idMap.put(curId, path);
        pathMap.put(path.toString(), curId);
        HashSet<Integer> nodes = ((MyPath) path).getDistinctNode();
        for (Integer node : nodes) {
            if (distinctNode.containsKey(node)) {
                distinctNode.put(node, distinctNode.get(node) + 1);
            } else {
                distinctNode.put(node, 1);
            }
        }
        return curId;
    }

    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid()
                || !pathMap.containsKey(path.toString())) {
            throw new PathNotFoundException(path);
        }
        int id = pathMap.get(path.toString());
        try {
            this.removePathById(id);
        } catch (PathIdNotFoundException e) {
            System.out.println(e.getStackTrace());
        }
        return id;
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!idMap.containsKey(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }
        Path path = idMap.get(pathId);
        idMap.remove(pathId);
        pathMap.remove(path.toString());
        HashSet<Integer> nodes = ((MyPath) path).getDistinctNode();
        for (int node : nodes) {
            int degree = distinctNode.get(node);
            if (degree == 1) {
                distinctNode.remove(node);
            } else {
                distinctNode.put(node, degree - 1);
            }
        }

    }

    public int getDistinctNodeCount() {
        return distinctNode.size();
    }   //在容器全局范围内查找不同的节点数
}

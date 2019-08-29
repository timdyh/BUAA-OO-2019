package path;

import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.HashMap;

public class MyPathContainer implements PathContainer {
    private int id = 0;
    private HashMap<Path, Integer> pathToId = new HashMap<>();
    private HashMap<Integer, Path> idToPath = new HashMap<>();
    private HashMap<Integer, Integer> distinctInteger = new HashMap<>();

    public MyPathContainer() {}

    public int nextId() {
        id += 1;
        return id;
    }

    public void addDistinctInteger(Path path) {
        for (Integer node: path) {
            if (distinctInteger.containsKey(node)) {
                int count;
                count = distinctInteger.get(node);
                distinctInteger.put(node, count + 1);
            } else {
                distinctInteger.put(node, 1);
            }
        }
    }

    public void removeDistinctInteger(Path path) {
        for (Integer node: path) {
            int count;
            count = distinctInteger.get(node);
            count -= 1;
            if (count == 0) {
                distinctInteger.remove(node);
            } else {
                distinctInteger.put(node, count);
            }
        }
    }

    public int size() {
        return pathToId.size();
    }

    public boolean containsPath(Path path) {
        return pathToId.containsKey(path);
    }

    public boolean containsPathId(int pathId) {
        return idToPath.containsKey(pathId);
    }

    public Path getPathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }

        return idToPath.get(pathId);
    }

    public int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }

        return pathToId.get(path);
    }

    public int addPath(Path path) {
        if (path == null || !path.isValid()) {
            return 0;
        }

        if (!containsPath(path)) {
            int pathId;
            pathId = nextId();
            pathToId.put(path, pathId);
            idToPath.put(pathId, path);

            addDistinctInteger(path);
        }

        return pathToId.get(path);
    }

    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }

        int pathId;
        pathId = pathToId.remove(path);
        idToPath.remove(pathId);
        removeDistinctInteger(path);

        return pathId;
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        }

        Path path;
        path = idToPath.remove(pathId);
        pathToId.remove(path);

        removeDistinctInteger(path);
    }

    public int getDistinctNodeCount() {
        return distinctInteger.size();
    }
}

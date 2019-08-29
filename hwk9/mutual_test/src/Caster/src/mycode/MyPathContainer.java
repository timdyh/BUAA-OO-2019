package mycode;

import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPathContainer implements PathContainer {
    private ArrayList<Path> pathList;
    private ArrayList<Integer> pidList;
    private HashMap<Path, Integer> pathToInt;
    private HashMap<Integer, Path> intToPath;
    private HashMap<Integer, Integer> nodeCount;
    private int nextId;

    public MyPathContainer() {
        pathList = new ArrayList<Path>();
        pidList = new ArrayList<Integer>();
        pathToInt = new HashMap<Path, Integer>();
        intToPath = new HashMap<Integer, Path>();
        nodeCount = new HashMap<Integer, Integer>();
        nextId = 1;
    }

    private int getNextId() {
        return nextId++;
    }

    public /*@pure@*/int size() {
        return pidList.size();
    }

    public /*@pure@*/ boolean containsPath(Path path) {
        if (path != null) {
            if (pathToInt.get(path) == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public /*@pure@*/ boolean containsPathId(int pathId) {
        if (intToPath.get(pathId) == null) {
            return false;
        } else {
            return true;
        }
    }

    public /*@pure@*/ Path getPathById(int pathId)
            throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            return intToPath.get(pathId);
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    public /*@pure@*/ int getPathId(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            return pathToInt.get(path);
        } else {
            throw new PathNotFoundException(path);
        }
    }

    public int addPath(Path path) {
        if (path != null && path.isValid()) {
            if (!containsPath(path)) {
                return updateByAddPath(path);
            } else {
                return pathToInt.get(path);
            }
        } else {
            return 0;
        }
    }

    public int removePath(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            int removeId = pathToInt.get(path);
            updateByRemovePath(path);
            return removeId;
        } else {
            throw new PathNotFoundException(path);
        }
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            Path curPath = getPathById(pathId);
            updateByRemovePath(curPath);
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    private int updateByAddPath(Path path) {
        int curNextId = getNextId();
        pidList.add(curNextId);
        pathList.add(path);
        pathToInt.put(path, curNextId);
        intToPath.put(curNextId, path);
        for (int e : path) {
            if (nodeCount.containsKey(e)) {
                int oldValue = nodeCount.get(e);
                nodeCount.replace(e, oldValue + 1);
            } else {
                nodeCount.put(e, 1);
            }
        }
        return curNextId;
    }

    private void updateByRemovePath(Path path) {
        int pathId = pathToInt.get(path);
        int removePos = pidList.indexOf(pathId);
        pidList.remove(removePos);
        pathList.remove(removePos);
        intToPath.remove(pathId);
        pathToInt.remove(path);
        for (int e : path) {
            int oldValue = nodeCount.get(e);
            if (oldValue > 1) {
                nodeCount.replace(e, oldValue - 1);
            } else {
                nodeCount.remove(e);
            }
        }
    }

    public /*@pure@*/int getDistinctNodeCount() {
        return nodeCount.size();
    }
}

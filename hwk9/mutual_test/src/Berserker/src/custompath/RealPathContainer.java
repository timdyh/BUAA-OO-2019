package custompath;

import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.HashMap;

public class RealPathContainer implements PathContainer {
    private HashMap<Integer,Path> container;
    private HashMap<Path,Integer> revcontainer;
    private HashMap<Integer,Integer> nodes;
    private int counter;

    public RealPathContainer() {
        counter = 0;
        container = new HashMap<>(10);
        revcontainer = new HashMap<>(10);
        nodes = new HashMap<>(10);
    }

    @Override
    public int size() {
        return container.size();
    }

    @Override
    public boolean containsPath(Path path) {
        if (path == null) { return false; }
        return revcontainer.containsKey(path);
    }

    @Override
    public boolean containsPathId(int i) {
        return container.containsKey(i);
    }

    @Override
    public Path getPathById(int i) throws Exception {
        if (!containsPathId(i)) { throw new PathIdNotFoundException(i); }
        return container.get(i);
    }

    @Override
    public int getPathId(Path path) throws Exception {
        if ((path == null) || (!path.isValid()) || (!containsPath(path)))
        {
            throw new PathNotFoundException(path);
        }
        return revcontainer.get(path);
    }

    @Override
    public int addPath(Path path) {
        if ((path == null) || (!path.isValid())) { return 0; }
        try {
            return getPathId(path);
        } catch (Exception e) {
            counter++;
            container.put(counter,path);
            revcontainer.put(path,counter);
            for (Integer i : path) {
                if (nodes.containsKey(i)) {
                    nodes.put(i,nodes.get(i) + 1);
                } else {
                    nodes.put(i,1);
                }
            }
            return counter;
        }
    }

    @Override
    public int removePath(Path path) throws Exception {
        int id = getPathId(path);
        container.remove(id);
        revcontainer.remove(path);
        for (Integer i : path) {
            if (nodes.get(i).equals(1)) {
                nodes.remove(i);
            } else {
                nodes.put(i,nodes.get(i) - 1);
            }
        }
        return id;
    }

    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        Path p = null;
        try {
            p = getPathById(i);
        } catch (Exception e) {
            throw new PathIdNotFoundException(i);
        }
        container.remove(i);
        revcontainer.remove(p);
        for (Integer j : p) {
            if (nodes.get(j).equals(1)) {
                nodes.remove(j);
            } else {
                nodes.put(j,nodes.get(j) - 1);
            }
        }
    }

    @Override
    public int getDistinctNodeCount() {
        return nodes.size();
    }
}

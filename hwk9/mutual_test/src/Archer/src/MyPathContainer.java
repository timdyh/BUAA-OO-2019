import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;
import java.util.HashMap;

public class MyPathContainer implements PathContainer {
    private HashMap<Integer,Path> plist;
    private HashMap<Integer,Integer> diff;
    private int idnow;

    public MyPathContainer() {
        plist = new HashMap<>();
        diff = new HashMap<>();
        idnow = 0;
    }

    public int size() {
        return plist.size();
    }

    public boolean containsPath(Path path) {
        return plist.containsValue(path);
    }

    public boolean containsPathId(int pathId) {
        return plist.containsKey(pathId);
    }

    public Path getPathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            return plist.get(pathId);
        }
        else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    public int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }
        else {
            return search(path);
        }
    }

    public int addPath(Path path) {
        if (path == null || !path.isValid()) {
            return 0;
        }
        else {
            int idtemp = search(path);
            if (idtemp != -1) {
                return idtemp;
            }
            else {
                idnow += 1;
                plist.put(idnow,path);
                for (Integer integer : path) {
                    int temp;
                    if (diff.containsKey(integer)) {
                        temp = diff.get(integer);
                    }
                    else {
                        temp = 0;
                    }
                    diff.put(integer,temp + 1);
                }
                return idnow;
            }
        }
    }

    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }
        else {
            int idtemp = search(path);
            plist.remove(idtemp);
            for (Integer integer : path) {
                int temp;
                if (diff.containsKey(integer)) {
                    temp = diff.get(integer) - 1;
                    if (temp == 0) {
                        diff.remove(integer);
                    }
                    else {

                        diff.put(integer,temp);
                    }
                }
                else {
                    System.out.println("fuck!it must be in the contain!");
                }
            }
            return idtemp;
        }
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        } else {
            Path pathtemp = plist.get(pathId);
            plist.remove(pathId);
            for (Integer integer : pathtemp) {
                int temp;
                if (diff.containsKey(integer)) {
                    temp = diff.get(integer) - 1;
                    if (temp == 0) {
                        diff.remove(integer);
                    }
                    else {

                        diff.put(integer,temp);
                    }
                }
                else {
                    System.out.println("fuck!it must be in the contain!");
                }
            }
        }
        return;
    }

    public int getDistinctNodeCount() {
        return diff.size();
    }

    private int search(Path path) {
        for (int i = 1;i <= idnow;i += 1) {
            if (containsPathId(i) && plist.get(i).equals(path)) {
                return i;
            }
        }
        return -1;
    }
}

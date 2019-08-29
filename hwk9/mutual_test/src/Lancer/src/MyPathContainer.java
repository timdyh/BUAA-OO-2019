import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;
import java.util.HashMap;

public class MyPathContainer implements PathContainer {

    private static int idCount = 1;
    //@ public instance model non_null Path[] pList;
    //@ public instance model non_null int[] pidList;
    private HashMap<Integer, Path> hashMapId2Path;
    private HashMap<Path, Integer> hashMapPath2Id;
    private HashMap<Integer, Integer> hashMapCount;

    public MyPathContainer() {
        hashMapId2Path = new HashMap<>();
        hashMapPath2Id = new HashMap<>();
        hashMapCount = new HashMap<>();
    }

    @Override
    //@ ensures \result == pList.length;
    public int size() {
        return hashMapId2Path.size();
    }

    @Override
    /*@ requires path != null;
      @ assignable \nothing;
      @ ensures \result == (\exists int i; 0 <= i && i < pList.length;
      @                     pList[i].equals(path));
      @*/
    public boolean containsPath(Path path) {
        return hashMapPath2Id.containsKey(path);
    }

    @Override
    /*@ ensures \result == (\exists int i; 0 <= i && i < pidList.length;
      @                      pidList[i] == pathId);
      @*/
    public boolean containsPathId(int i) {
        return hashMapId2Path.containsKey(i);
    }

    @Override
    /*@ public normal_behavior
      @ requires containsPathId(pathId);
      @ assignable \nothing;
      @ ensures (pidList.length == pList.length)
      && (\exists int i; 0 <= i && i < pList.length;
      pidList[i] == pathId && \result == pList[i]);
      @ also
      @ public exceptional_behavior
      @ requires !containsPathId(pathId);
      @ assignable \nothing;
      @ signals_only PathIdNotFoundException;
      @*/
    public Path getPathById(int i) throws Exception {
        if (!this.containsPathId(i)) {
            throw new PathIdNotFoundException(i);
        }

        return hashMapId2Path.get(i);
    }

    @Override
    /*@ public normal_behavior
      @ requires path != null && path.isValid() && containsPath(path);
      @ assignable \nothing;
      @ ensures (pidList.length == pList.length) && (
      \exists int i; 0 <= i && i < pList.length; pList[i].equals(path)
      && pidList[i] == \result);
      @ also
      @ public exceptional_behavior
      @ signals (PathNotFoundException e) path == null;
      @ signals (PathNotFoundException e) !path.isValid();
      @ signals (PathNotFoundException e) !containsPath(path);
      @*/
    public int getPathId(Path path) throws Exception {
        if (path == null || !path.isValid() ||
                !hashMapPath2Id.containsKey(path)) {
            throw new PathNotFoundException(path);
        }
        return hashMapPath2Id.get(path);
    }

    @Override
    /*@ normal_behavior
      @ requires path != null && path.isValid();
      @ assignable pList, pidList;
      @ ensures (pidList.length == pList.length);
      @ ensures (\exists int i; 0 <= i && i <
      pList.length; pList[i] == path &&
      @           \result == pidList[i]);
      @ ensures !\old(containsPath(path)) ==>
      @          pList.length == (\old(pList.length) + 1) &&
      @          pidList.length == (\old(pidList.length) + 1);
      @ ensures (\forall int i; 0 <= i && i < \old(pList.length);
      @          containsPath(\old(pList[i])) &&
      containsPathId(\old(pidList[i])));
      @ also
      @ normal_behavior
      @ requires path == null || path.isValid() == false;
      @ assignable \nothing;
      @ ensures \result == 0;
      @*/
    public int addPath(Path path) throws Exception {
        if (path == null || !path.isValid()) {
            return 0;
        }
        if (hashMapPath2Id.containsKey(path)) {
            return hashMapPath2Id.get(path);
        }
        hashMapPath2Id.put(path, idCount);
        hashMapId2Path.put(idCount, path);
        for (Integer integer : path) {
            if (hashMapCount.containsKey(integer)) {
                int tem = hashMapCount.get(integer);
                hashMapCount.put(integer, tem + 1);
            } else {
                hashMapCount.put(integer, 1);
            }
        }
        idCount++;
        return idCount - 1;
    }

    @Override
    /*@ public normal_behavior
      @ requires path != null && path.isValid() && \old(containsPath(path));
      @ assignable pList, pidList;
      @ ensures containsPath(path) == false;
      @ ensures (pidList.length == pList.length);
      @ ensures (\exists int i; 0 <= i && i < \old(pList.length);
      \old(pList[i].equals(path)) &&
      @           \result == \old(pidList[i]));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathNotFoundException e) path == null;
      @ signals (PathNotFoundException e) path.isValid()==false;
      @ signals (PathNotFoundException e) !containsPath(path);
      @*/
    public int removePath(Path path) throws Exception {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }
        int id = hashMapPath2Id.get(path);
        hashMapPath2Id.remove(path);
        hashMapId2Path.remove(id);
        for (Integer integer : path) {
            int tem = hashMapCount.get(integer);
            if (tem == 1) {
                hashMapCount.remove(integer);
            } else if (tem > 1) {
                hashMapCount.put(integer, tem - 1);
            }
        }
        return id;
    }

    @Override
    /*@ public normal_behavior
      @ requires \old(containsPathId(pathId));
      @ assignable pList, pidList;
      @ ensures pList.length == pidList.length;
      @ ensures (\forall int i; 0 <= i && i < pidList.length;
      pidList[i] != pathId);
      @ ensures (\forall int i; 0 <= i && i < pList.length;
      !pList[i].equals(\old(getPathById(pathId))));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathIdNotFoundException e) !containsPathId(pathId);
      @*/
    public void removePathById(int i) throws PathIdNotFoundException {
        if (!containsPathId(i)) {
            throw new PathIdNotFoundException(i);
        }
        Path path = hashMapId2Path.get(i);
        hashMapId2Path.remove(i);
        hashMapPath2Id.remove(path);
        for (Integer integer : path) {
            int tem = hashMapCount.get(integer);
            if (tem == 1) {
                hashMapCount.remove(integer);
            } else if (tem > 1) {
                hashMapCount.put(integer, tem - 1);
            }
        }
    }

    @Override
    /*@ ensures (\exists int[] arr; (\forall int i, j;
    0 <= i && i < j && j < arr.length; arr[i] != arr[j]);
@(\forall int i; 0 <= i && i < arr.length;
(\exists Path p; this.containsPath(p); p.containsNode(arr[i])))
@            &&(\forall Path p; this.containsPath(p);
(\forall int node; p.containsNode(node); (\exists int i;
0 <= i && i < arr.length; node == arr[i])))
 @            &&(\result == arr.length));
 @*/
    public int getDistinctNodeCount() {
        return hashMapCount.size();
        /*ArrayList<Integer> tem = new ArrayList<>();
        for (Path path : hashMapPath2Id.keySet()) {
            for (Integer integer : path) {
                if (!tem.contains(integer)) {
                    tem.add(integer);
                }
            }
        }
        return tem.size();*/
    }
    // TODO : IMPLEMENT
}

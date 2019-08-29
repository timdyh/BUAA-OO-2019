import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.HashMap;
import java.util.HashSet;

public class MyPathContainer2 implements PathContainer {

    //@ public instance model non_null Path[] pList;
    //@ public instance model non_null int[] pidList;

    private HashMap<Integer, Path> pathList;
    private HashMap<Path, Integer> pidlList;
    private HashSet<Integer> count;
    private int index;

    public MyPathContainer2() {
        this.pathList = new HashMap<>();
        this.pidlList = new HashMap<>();
        this.count = new HashSet<>();
        index = 0;
    }

    //@ ensures \result == pList.length;
    public /*@pure@*/int size() {
        return pathList.size();
    }

    /*@ requires path != null;
      @ assignable \nothing;
      @ ensures \result == (\exists int i; 0 <= i && i < pList.length;
      @                     pList[i].equals(path));
      @*/
    public /*@pure@*/ boolean containsPath(Path path) {
        return pathList.containsValue(path);
    }

    /*@ ensures \result == (\exists int i; 0 <= i && i < pidList.length;
      @                      pidList[i] == pathId);
      @*/
    public /*@pure@*/ boolean containsPathId(int pathId) {
        return pathList.containsKey(pathId);
    }

    /*@ public normal_behavior
      @ requires containsPathId(pathId);
      @ assignable \nothing;
      @ ensures (pidList.length == pList.length)&&(\exists int i; 0 <= i &&
      i < pList.length; pidList[i] == pathId && \result == pList[i]);
      @ also
      @ public exceptional_behavior
      @ requires !containsPathId(pathId);
      @ assignable \nothing;
      @ signals_only PathIdNotFoundException;
      @*/
    public /*@pure@*/ Path getPathById(int pathId)
            throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            return pathList.get(pathId);
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    /*@ public normal_behavior
      @ requires path != null && path.isValid() && containsPath(path);
      @ assignable \nothing;
      @ ensures (pidList.length == pList.length) && (\exists int i; 0 <= i
      && i < pList.length; pList[i].equals(path) && pidList[i] == \result);
      @ also
      @ public exceptional_behavior
      @ signals (PathNotFoundException e) path == null;
      @ signals (PathNotFoundException e) !path.isValid();
      @ signals (PathNotFoundException e) !containsPath(path);
      @*/
    public /*@pure@*/ int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        } else {
            return pidlList.get(path);
        }
    }

    /*@ normal_behavior
      @ requires path != null && path.isValid();
      @ assignable pList, pidList;
      @ ensures (pidList.length == pList.length);
      @ ensures (\exists int i; 0 <= i && i < pList.length; pList[i] == path &&
      @           \result == pidList[i]);
      @ ensures !\old(containsPath(path)) ==>
      @          pList.length == (\old(pList.length) + 1) &&
      @          pidList.length == (\old(pidList.length) + 1);
      @ ensures (\forall int i; 0 <= i && i < \old(pList.length);
      @          containsPath(\old(pList[i]))
      && containsPathId(\old(pidList[i])));
      @ also
      @ normal_behavior
      @ requires path == null || path.isValid() == false;
      @ assignable \nothing;
      @ ensures \result == 0;
      @*/
    public int addPath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid()) {
            return 0;
        }
        if (!containsPath(path)) {
            index++;
            pathList.put(index, path);
            pidlList.put(path, index);
            for (int i = 0; i < path.size(); i++) {
                count.add(path.getNode(i));
            }
            return index;
        } else {
            return getPathId(path);
        }

    }

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
    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        } else {
            int k = pidlList.remove(path);
            pathList.remove(k);
            for (int i = 0; i < path.size(); i++) {
                int flag = 0;
                for (Integer key : pathList.keySet()) {
                    if (pathList.get(key).containsNode(path.getNode(i))) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    count.remove(new Integer(path.getNode(i)));
                }
            }
            return k;
        }
    }

    /*@ public normal_behavior
      @ requires \old(containsPathId(pathId));
      @ assignable pList, pidList;
      @ ensures pList.length == pidList.length;
      @ ensures (\forall int i; 0 <= i &&
      i < pidList.length; pidList[i] != pathId);
      @ ensures (\forall int i; 0 <= i &&
      i < pList.length;!pList[i].equals(\old(getPathById(pathId))));
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ signals (PathIdNotFoundException e) !containsPathId(pathId);
      @*/
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        } else {
            Path x = pathList.remove(pathId);
            pidlList.remove(x);
            for (int i = 0; i < x.size(); i++) {
                int flag = 0;
                for (Integer key : pathList.keySet()) {
                    if (pathList.get(key).containsNode(x.getNode(i))) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    count.remove(new Integer(x.getNode(i)));
                }
            }
        }
    }

    /*@ ensures \result == (\num_of int[] nlist;
           (\forall int i,j; 0 <= i &&
           i < pList.length && 0 <= j && j < pList[i].size();
           (\exists int k; 0 <= k
           && k < nlist.length; nlist[k] == pList[i].getNode(j)));
           (\forall int m, n; 0 <= m && m < n
           && n < nlist.length; nlist[m] != nlist[n]));
      @*/

    public /*@pure@*/int getDistinctNodeCount() //在容器全局范围内查找不同的节点数
    {
        return count.size();
    }
}

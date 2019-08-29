import com.oocourse.specs1.models.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MyPath implements Path {

    private ArrayList<Integer> arrayList = new ArrayList<>();
    private int distinctCount = 0;
    private int hascode = 0;
    private HashSet<Integer> hashSet = new HashSet<>();

    public boolean helpFunction(int i) {
        if (hashSet.size() == 0) {
            hashSet.addAll(arrayList);
        }
        return hashSet.contains(i);
    }

    public MyPath(int... nodeList) {
        for (int i1 : nodeList) {
            arrayList.add(i1);
        }
        ArrayList<Integer> temArray = new ArrayList<>();
        for (Integer integer : arrayList) {
            if (!temArray.contains(integer)) {
                temArray.add(integer);
            }
        }
        distinctCount = temArray.size();
        hascode = arrayList.hashCode();
    }

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public int getNode(int i) {
        return arrayList.get(i);
    }

    @Override
    public boolean containsNode(int i) {
        return this.helpFunction(i);
    }

    @Override
    /*@ ensures (\exists int[] arr; (\forall int i, j;
    0 <= i && i < j && j < arr.length; arr[i] != arr[j]);
      @             (\forall int i; 0 <= i && i <
      arr.length;this.containsNode(arr[i]))
      @           && (\forall int node; this.containsNode(node);
      (\exists int j; 0 <= j && j < arr.length; arr[j] == node))
      @           && (\result == arr.length));
      @*/
    public int getDistinctNodeCount() {
        return distinctCount;
    }

    @Override
    //@ ensures \result == (nodes.length >= 2);
    public boolean isValid() {
        return arrayList.size() >= 2;
    }

    @Override
    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param   o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *          is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     */
    public int compareTo(Path o) throws
            NullPointerException, ClassCastException {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!(o instanceof Path)) {
            throw new ClassCastException();
        }
        for (int i = 0; i < this.size() && i < o.size(); i++) {
            if (this.getNode(i) < o.getNode(i)) {
                return -1;
            } else if (this.getNode(i) > o.getNode(i)) {
                return 1;
            } else {
                continue;
            }
        }
        return Integer.compare(this.size(), o.size());
    }

    @Override
    public Iterator<Integer> iterator() {
        return arrayList.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.size() != ((MyPath) o).size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (this.getNode(i) != ((MyPath) o).getNode(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return hascode;
    }

    @Override
    public String toString() {
        String string = "";
        for (Integer integer : arrayList) {
            string = string + integer;
        }
        return string;
    }
    // TODO : IMPLEMENT
}

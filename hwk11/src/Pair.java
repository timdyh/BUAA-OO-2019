public class Pair {
    private int node1;
    private int node2;
    private int hashcode;

    public Pair(int node1, int node2) {
        this.node1 = node1;
        this.node2 = node2;
        this.hashcode = node1 + node2 + node1 * node2;
    }

    public int getNode1() {
        return node1;
    }

    public int getNode2() {
        return node2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            return (((Pair) obj).getNode1() == this.getNode1()
                    && ((Pair) obj).getNode2() == this.getNode2())
                    || (((Pair) obj).getNode1() == this.getNode2()
                    && ((Pair) obj).getNode2() == this.getNode1());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public String toString() {
        return "Pair(" + node1 + ", " + node2 + ")";
    }
}
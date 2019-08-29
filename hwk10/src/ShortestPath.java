public class ShortestPath {
    private int node1;
    private int node2;
    private int hashcode;

    public ShortestPath(int node1, int node2) {
        this.node1 = node1;
        this.node2 = node2;
        this.hashcode = node1 + node2 + node1 * node2;
    }

    private int getNode1() {
        return node1;
    }

    private int getNode2() {
        return node2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShortestPath) {
            return (((ShortestPath) obj).getNode1() == this.getNode1()
                    && ((ShortestPath) obj).getNode2() == this.getNode2())
                    || (((ShortestPath) obj).getNode1() == this.getNode2()
                    && ((ShortestPath) obj).getNode2() == this.getNode1());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }
}

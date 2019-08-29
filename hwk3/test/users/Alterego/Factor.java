public abstract class Factor {
    private String str;

    public Factor(String str) {
        this.str = str;
    }

    public abstract String diff();

    public String toString() {
        return str;
    }
}
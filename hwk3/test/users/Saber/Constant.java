public class Constant implements Father {
    private String exp;

    Constant(String line) {
        this.exp = line;
    }

    public String derivate() {
        return "0";
    }

    public String print() {
        return this.exp;
    }
}

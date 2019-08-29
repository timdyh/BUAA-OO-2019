public class OutputHandler {
    private String cleanPoly;

    public OutputHandler(String str) {
        String out = str;

        if (out.startsWith("(") && out.endsWith(")")) {
            out = out.substring(1, str.length() - 1);
        }

        out = out.replaceAll("\\+\\-", "-");
        this.cleanPoly = out;
    }

    protected String print() {
        return cleanPoly;
    }
}

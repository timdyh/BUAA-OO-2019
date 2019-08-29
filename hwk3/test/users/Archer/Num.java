package poly;

import java.util.regex.Pattern;

public class Num extends Func {
    private String s1 = new String();

    public Num() {
    }

    public Num(String s) {
        s1 = s;
    }

    public String dri() {
        Pattern p = Pattern.compile("");
        if (Pattern.matches("[+-][+-]?\\d+", s1)) {
            return "+0";
        } else {
            return "0";
        }
    }

    public String print() {
        return s1;
    }
}

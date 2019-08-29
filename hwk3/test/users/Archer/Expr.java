package poly;

public class Expr extends Func {
    private String s1 = new String();

    public Expr() {
    }

    public Expr(String s) {
        s1 = s;
    }

    public String dri() {
        String pt = "";
        String s2 = new String();
        if (s1.charAt(0) == '+' || s1.charAt(0) == '-') {
            s2 = s1.substring(2, s1.length() - 1);
        } else {
            s2 = s1.substring(1, s1.length() - 1);
        }
        Items items1 = new Items(s2);
        if (s1.charAt(0) == '+') {
            pt = pt + "+";
        } else if (s1.charAt(0) == '-') {
            pt = pt + "-";
        }
        pt = pt + items1.dri();
        return pt;
    }

    public String print() {
        return s1;
    }
}


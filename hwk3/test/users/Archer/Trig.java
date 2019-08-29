package poly;

import java.math.BigInteger;
import java.util.regex.Pattern;

import static poly.Item.rightK;
import static poly.Main.checkFail;

public class Trig extends Func {
    private String s1 = new String();
    private String s2 = new String();
    private String s3 = new String();
    private BigInteger index = BigInteger.ONE;

    public Trig() {
    }

    public Trig(String s) {
        s1 = s;
        int end = rightK(s);
        if (s1.charAt(0) == '+' || s1.charAt(0) == '-') {
            s2 = s1.substring(5, end);
            s3 = s1.substring(1, 4);
        } else {
            s2 = s1.substring(4, end);
            s3 = s1.substring(0, 3);
        }
        if (s2.charAt(0) != '(') {
            if (!Pattern.matches("[+-]?\\d+", s2)) {
                if (!Pattern.matches("x(\\^[+-]?\\d+)?", s2)) {
                    if (s2.length() > 3) {
                        if (!s2.substring(0, 3).equals("sin")
                                && !s2.substring(0, 3).equals("cos")) {
                            checkFail();
                        }
                    } else {
                        checkFail();
                    }
                }
            }
        }
        if ((end + 1) != s.length()) {
            index = new BigInteger(s1.substring(end + 2));
            if (index.compareTo(new BigInteger("10000")) == 1) {
                checkFail();
            }
        }
    }

    public String dri() {
        String pt = "";
        if (s1.charAt(0) == '+') {
            pt = pt + "+";
        } else if (s1.charAt(0) == '-') {
            pt = pt + "-";
        }
        pt = pt + index.toString() + "*";
        if (s3.equals("sin")) {
            pt = pt + "sin(" + s2 + ")";
        } else if (s3.equals("cos")) {
            pt = pt + "cos(" + s2 + ")";
        }
        pt = pt + "^" + index.subtract(BigInteger.ONE).toString();
        if (s3.equals("sin")) {
            pt = pt + "*cos(" + s2 + ")";
        } else if (s3.equals("cos")) {
            pt = pt + "*-1*sin(" + s2 + ")";
        }
        Items items = new Items(s2);
        pt = pt + "*" + items.dri();
        return pt;
    }

    public String print() {
        return s1;
    }

}

package poly;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Power extends Func {
    private String s1 = new String();
    private BigInteger index = BigInteger.ONE;

    public Power() {
    }

    public Power(String s) {
        s1 = s;
        Pattern p = Pattern.compile("([+-]?)(x)((\\^[+-]?\\d+)?)");
        Matcher m = p.matcher(s1);
        if (m.find()) {
            Pattern p1 = Pattern.compile("[+-]?\\d+");
            Matcher m1 = p1.matcher(s1);
            if (m1.find()) {
                index = new BigInteger(m1.group());
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
        pt = pt + index.toString() + "*x^" +
                index.subtract(BigInteger.ONE).toString();
        return pt;
    }

    public String print() {
        return s1;
    }
}

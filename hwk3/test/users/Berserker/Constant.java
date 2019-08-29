package homework;

import java.math.BigInteger;

public class Constant extends Function {
    public Constant(String str, int neg) {
        super.setIndex(BigInteger.ZERO);
        super.setCoef(new BigInteger(str).multiply(BigInteger.valueOf(neg)));
    }

    public String getString() {
        return super.getCoef().toString();
    }

    public String dev() {
        String res = "0";
        return res;
    }
}

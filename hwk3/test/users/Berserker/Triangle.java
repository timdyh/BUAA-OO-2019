package homework;

import java.math.BigInteger;

public class Triangle extends Function {
    private boolean sin = true;

    public Triangle(String str, int neg) {
        char[] strArray = str.toCharArray();
        String coefTemp = "";
        for (int i = 0; strArray[i] != 'i' && strArray[i] != 'o'; i++) {
            if (strArray[i] == 'c') {
                sin = false;
                continue;
            } else if (strArray[i] == 's') {
                continue;
            }
            coefTemp += strArray[i];
        }
        if (coefTemp.equals("-")) {
            super.setCoef(new BigInteger("-1")
                    .multiply(BigInteger.valueOf(neg)));
        } else if (coefTemp.equals("+") || coefTemp.equals("")) {
            super.setCoef(BigInteger.valueOf(neg));
        } else {
            super.setCoef(new BigInteger(coefTemp)
                    .multiply(BigInteger.valueOf(neg)));
        }
        int i;
        for (i = strArray.length - 1; strArray[i] != ')'
                && strArray[i] != '^'; i--) {};
        if (i == strArray.length - 1) {
            super.setIndex(BigInteger.ONE);
        } else {
            super.setIndex(new BigInteger(str.substring(i + 1)));
        }
        if (super.getIndex().compareTo(BigInteger.valueOf(10000)) > 0) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }

    public String getString() {
        String coefTemp = super.getCoef().toString() + "*";
        if (super.getCoef().compareTo(BigInteger.ONE) == 0) {
            coefTemp = "";
        }
        String triangle = "cos(x)";
        if (sin) {
            triangle = "sin(x)";
        }
        if (super.getIndex().compareTo(BigInteger.ONE) == 0) {
            return coefTemp + triangle;
        } else {
            return coefTemp + triangle + super.getIndex().toString();
        }
    }

    public boolean getSin() {
        return sin;
    }

    public String dev() {
        String res = "";
        String tri = "";
        String triDer = "";
        int neg = 1;
        if (sin) {
            tri = "sin(x)";
            triDer = "cos(x)";
        } else {
            neg = -1;
            triDer = "sin(x)";
            tri = "cos(x)";
        }
        String coefTemp = "";
        String indexTemp = "";
        if (super.getIndex().compareTo(BigInteger.ZERO) == 0) {
            coefTemp += "0";
        } else {
            coefTemp += super.getCoef().multiply(super.getIndex())
                    .multiply(BigInteger.valueOf(neg)).toString();
            indexTemp += super.getIndex().subtract(BigInteger.ONE);
        }
        if (coefTemp.equals("1")) {
            res += tri + "^" + indexTemp + "*" + triDer;
        } else if (coefTemp.equals("-1")) {
            res += "-" + tri + "^" + indexTemp + "*" + triDer;
        } else {
            res += coefTemp + tri + "^" + indexTemp + "*" + triDer;
        }
        return res;
    }
}

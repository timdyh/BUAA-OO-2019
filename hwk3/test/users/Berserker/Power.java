package homework;

import java.math.BigInteger;

public class Power extends Function {
    public Power(String str, int neg) {
        char[] strArray = str.toCharArray();
        String coefTemp = "";
        for (int i = 0; strArray[i] != 'x'; i++) {
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
        String indexTemp = "";
        int i;
        for (i = strArray.length - 1; strArray[i] != 'x'
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
        if (super.getIndex().compareTo(BigInteger.ONE) == 0) {
            return coefTemp + "x";
        } else {
            return coefTemp + "x^" + super.getIndex().toString();
        }
    }

    public String dev() {
        String res = "";
        String coefTemp = "";
        String indexTemp = "";
        if (super.getIndex().compareTo(BigInteger.ZERO) == 0) {
            coefTemp += "0";
        } else {
            coefTemp += super.getCoef().multiply(super.getIndex()).toString();
            indexTemp += super.getIndex().subtract(BigInteger.ONE);
        }
        if (coefTemp.equals("1")) {
            res += "x^" + indexTemp;
        } else if (coefTemp.equals("-1")) {
            res += "-x^" + indexTemp;
        } else {
            res += coefTemp + "x^" + indexTemp;
        }
        return res;
    }
}


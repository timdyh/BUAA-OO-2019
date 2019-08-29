import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Term {
    private BigInteger coeff;
    private BigInteger indexX;
    private BigInteger indexSin;
    private BigInteger indexCos;

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger ZERO = BigInteger.ZERO;

    public Term(BigInteger k, BigInteger a, BigInteger b, BigInteger c) {
        coeff = k;
        indexX = a;
        indexSin = b;
        indexCos = c;
    }

    public Term(String str) {
        coeff = ONE;
        indexX = ZERO;
        indexSin = ZERO;
        indexCos = ZERO;

        for (int i = 0; i < 2; i++) {
            if (str.charAt(i) == '-') {
                coeff = coeff.negate();
            }
        }

        Pattern patternCon = Pattern.compile("\\*([-+]?\\d+)");
        Matcher matcherCon = patternCon.matcher(str);
        while (matcherCon.find()) {
            coeff = coeff.multiply(new BigInteger(matcherCon.group(1)));
        }

        Pattern patternPow = Pattern.compile("\\*x(\\^([-+]?\\d+))?");
        Matcher matcherPow = patternPow.matcher(str);
        while (matcherPow.find()) {
            if (matcherPow.group(2) == null) {
                indexX = indexX.add(ONE);
            } else {
                indexX = indexX.add(new BigInteger(matcherPow.group(2)));
            }
        }

        Pattern patternSin = Pattern.compile("\\*sin\\(x\\)(\\^([-+]?\\d+))?");
        Matcher matcherSin = patternSin.matcher(str);
        while (matcherSin.find()) {
            if (matcherSin.group(2) == null) {
                indexSin = indexSin.add(ONE);
            } else {
                indexSin = indexSin.add(new BigInteger(matcherSin.group(2)));
            }
        }

        Pattern patternCos = Pattern.compile("\\*cos\\(x\\)(\\^([-+]?\\d+))?");
        Matcher matcherCos = patternCos.matcher(str);
        while (matcherCos.find()) {
            if (matcherCos.group(2) == null) {
                indexCos = indexCos.add(ONE);
            } else {
                indexCos = indexCos.add(new BigInteger(matcherCos.group(2)));
            }
        }
    }

    public boolean isZero() {
        return coeff.equals(ZERO);
    }

    public BigInteger getCoeff() {
        return coeff;
    }

    public BigInteger getIndexX() {
        return indexX;
    }

    public BigInteger getIndexSin() {
        return indexSin;
    }

    public BigInteger getIndexCos() {
        return indexCos;
    }

    public String toString() {
        String str;
        if (coeff.signum() == 1) {
            str = "+";
        } else if (coeff.signum() == -1) {
            str = "-";
        } else {
            return "+0";
        }

        if (indexX.equals(ZERO) && indexSin.equals(ZERO) &&
                indexCos.equals(ZERO) && coeff.abs().equals(ONE)) {
            str += "1";
            return str;
        }

        boolean flag = false;
        if (!coeff.abs().equals(ONE)) {
            str += coeff.abs();
            flag = true;
        }

        if (!indexX.equals(ZERO)) {
            if (flag) {
                str += "*";
            }
            str += "x";
            if (!indexX.equals(ONE)) {
                str += "^" + indexX.toString();
            }
            flag = true;
        }

        if (!indexSin.equals(ZERO)) {
            if (flag) {
                str += "*";
            }
            str += "sin(x)";
            if (!indexSin.equals(ONE)) {
                str += "^" + indexSin.toString();
            }
            flag = true;
        }

        if (!indexCos.equals(ZERO)) {
            if (flag) {
                str += "*";
            }
            str += "cos(x)";
            if (!indexCos.equals(ONE)) {
                str += "^" + indexCos.toString();
            }
        }

        return str;
    }
}
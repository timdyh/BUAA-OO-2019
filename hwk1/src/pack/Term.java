package pack;

import java.math.BigInteger;

class Term {
    private BigInteger coeff;
    private BigInteger index;

    public Term(BigInteger co, BigInteger in) {
        coeff = co;
        index = in;
    }

    public Term(String g1, String g2, String g3, String g4, String g6) {
        if (g2 == null && g3 == null) {
            coeff = new BigInteger("1");
        } else if (g2 == null && g3 != null) {
            coeff = new BigInteger(g3);
        } else if (g2 != null && g3 == null) {
            if (g2.equals("+")) {
                coeff = new BigInteger("1");
            } else {
                coeff = new BigInteger("-1");
            }
        } else {
            coeff = new BigInteger(g2 + g3);
        }

        if (g1.equals("-")) {
            coeff = coeff.negate();
        }

        if (g6 == null) {
            if (g3 != null) {
                if (g4 == null) {
                    index = new BigInteger("0");
                } else {
                    index = new BigInteger("1");
                }
            } else {
                index = new BigInteger("1");
            }
        } else {
            index = new BigInteger(g6);
        }
    }

    public boolean isZero() {
        return coeff.equals(BigInteger.ZERO);
    }

    public BigInteger getCoeff() {
        return coeff;
    }

    public BigInteger getIndex() {
        return index;
    }

    public Term diff() {
        coeff = coeff.multiply(index);
        index = index.subtract(new BigInteger("1"));
        return this;
    }

    public String toString() {
        String s;
        if (coeff.signum() == 1) {
            s = "+";
        } else if (coeff.signum() == -1) {
            s = "-";
            coeff = coeff.abs();
        } else {
            return "+0";
        }

        if (coeff.equals(BigInteger.ONE)) {
            if (index.equals(BigInteger.ZERO)) {
                s += "1";
            } else {
                s += "x";
            }
        } else {
            s += coeff.toString();
        }

        if (!index.equals(BigInteger.ZERO) && !coeff.equals(BigInteger.ONE)) {
            s += "*x";
        }

        if (!index.equals(BigInteger.ZERO) && !index.equals(BigInteger.ONE)) {
            s += "^" + index;
        }
        return s;
    }
}
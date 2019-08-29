import java.math.BigInteger;
import java.util.ArrayList;

public class Composite implements Factor {
    private Sin sinOut = null;
    private Cos cosOut = null;
    private Poly polyIn;
    
    Composite(int out,String expo,String factor) {
        String constant = "[+-]?\\d+";
        String sin = "sin\\(x\\)(\\^[+-]?\\d+)?";
        String cos = "cos\\(x\\)(\\^[+-]?\\d+)?";
        String xpow = "x(\\^[+-]?\\d+)?";
        String composite = "(sin|cos)\\((.*)\\)(\\^[+-]?\\d+)?";
        String poly = "\\((.*)\\)";
        if (new BigInteger(expo).compareTo(new BigInteger("10000")) > 0) {
            Wrong.WrongFormat();
        }
        if (out == 1) { //1:sin
            sinOut = new Sin(expo);
        } else if (out == 2) {
            cosOut = new Cos(expo);
        } else {
            Wrong.WrongFormat();
        }
        if (factor.matches(poly)) {
            polyIn = new Poly(factor.substring(1,factor.length() - 1));
        } else if (factor.matches(constant) || factor.matches(sin)
                || factor.matches(cos) || factor.matches(xpow)
                || factor.matches(composite)) {
            polyIn = new Poly(factor);
        } else {
            Wrong.WrongFormat();
        }
        /*else if (factor.matches(constant)) {
            factorIn = new Const(factor);
        } else if (factor.matches(sin)) {
            String sinExpo = Term.getExpo(factor,')');
            factorIn = new Sin(sinExpo);
        } else if (factor.matches(cos)) {
            String cosExpo = Term.getExpo(factor,')');
            factorIn = new Cos(cosExpo);
        } else if (factor.matches(xpow)) {
            String xpowExpo = Term.getExpo(factor,'x');
            factorIn = new Xpow(xpowExpo);
        } else if (factor.matches(composite)) {
            factorIn = new Composite(Term.getCompositeOut(factor),
                    Term.getExpo(factor,')'),
                    Term.getCompositeIn(factor));
        } */
    }
    
    public ArrayList<Factor> derivate() {
        ArrayList<Factor> derivation = new ArrayList<>();
        BigInteger newCoef = new BigInteger("0");
        BigInteger newExpo;
        int typeOut = 3;
        if (sinOut != null) {
            newCoef = this.sinOut.getSinExpo();
            typeOut = 1;
            derivation.add(new Composite(2,"1",
                    "(" + polyIn.getPoly() + ")"));
        } else if (cosOut != null) {
            newCoef = this.cosOut.getCosExpo();
            typeOut = 2;
            derivation.add(new Composite(1,"1",
                    "(" + polyIn.getPoly() + ")"));
        } else {
            Wrong.WrongFormat();
        }
        newExpo = newCoef.subtract(BigInteger.ONE);
        if (typeOut == 2) {
            newCoef = newCoef.negate();
        }
        derivation.add(new Const(newCoef.toString()));
        Composite deriCompo = new Composite(
                typeOut,newExpo.toString(),"(" + polyIn.getPoly() + ")");
        derivation.add(deriCompo);
        derivation.add(polyIn.derivate().get(0));
        return derivation;
    }
    
    public StringBuffer print() {
        StringBuffer output = new StringBuffer();
        BigInteger expo = new BigInteger("1");
        if (sinOut != null) {
            expo = sinOut.getSinExpo();
        } else if (cosOut != null) {
            expo = cosOut.getCosExpo();
        } else {
            Wrong.WrongFormat();
        }
        if (!expo.equals(BigInteger.ZERO)) {
            if (sinOut != null) {
                output.append("sin(");
            } else if (cosOut != null) {
                output.append("cos(");
            } else {
                Wrong.WrongFormat();
            }
            StringBuffer temp = polyIn.print();
            output.append(temp);
            output.append(")");
            if (!expo.equals(BigInteger.ONE)) {
                output.append("^");
                output.append(expo);
            }
        } else {
            output.append("1");
        }
        return output;
    }
}

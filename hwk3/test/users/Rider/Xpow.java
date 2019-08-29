import java.math.BigInteger;
import java.util.ArrayList;

public class Xpow implements Factor {
    private BigInteger xpowExpo;
    
    Xpow(String xpowExpo) {
        if (new BigInteger(xpowExpo).compareTo(
                new BigInteger("10000")) > 0) {
            Wrong.WrongFormat();
        }
        this.xpowExpo = new BigInteger(xpowExpo);
    }
    
    void setXpowExpo(BigInteger xpowExpo) {
        this.xpowExpo = xpowExpo;
    }
    
    BigInteger getXpowExpo() {
        return xpowExpo;
    }
    
    public ArrayList<Factor> derivate() {
        ArrayList<Factor> derivation = new ArrayList<>();
        BigInteger newCoef = this.xpowExpo;
        BigInteger newExpo = this.xpowExpo.subtract(BigInteger.ONE);
        derivation.add(new Const(newCoef.toString()));
        derivation.add(new Xpow(newExpo.toString()));
        return derivation;
    }
    
    public StringBuffer print() {
        StringBuffer output = new StringBuffer();
        if (!xpowExpo.equals(BigInteger.ZERO)) {
            output.append("x");
            if (!xpowExpo.equals(BigInteger.ONE)) {
                output.append("^");
                output.append(xpowExpo);
            }
        } else {
            output.append("1");
        }
        return output;
    }
}

import java.math.BigInteger;
import java.util.ArrayList;

public class Cos implements Factor {
    private BigInteger cosExpo;
    
    Cos(String cosExpo) {
        if (new BigInteger(cosExpo).compareTo(
                new BigInteger("10000")) > 0) {
            Wrong.WrongFormat();
        }
        this.cosExpo = new BigInteger(cosExpo);
    }
    
    void setCosExpo(BigInteger cosExpo) {
        this.cosExpo = cosExpo;
    }
    
    BigInteger getCosExpo() {
        return cosExpo;
    }
    
    public ArrayList<Factor> derivate() {
        ArrayList<Factor> derivation = new ArrayList<>();
        BigInteger newCoef = this.cosExpo.negate();
        BigInteger newExpo = this.cosExpo.subtract(BigInteger.ONE);
        derivation.add(new Const(newCoef.toString()));
        derivation.add(new Cos(newExpo.toString()));
        derivation.add(new Sin("1"));
        return derivation;
    }
    
    public StringBuffer print() {
        StringBuffer output = new StringBuffer();
        if (!cosExpo.equals(BigInteger.ZERO)) {
            output.append("cos(x)");
            if (!cosExpo.equals(BigInteger.ONE)) {
                output.append("^");
                output.append(cosExpo);
            }
        } else {
            output.append("1");
        }
        return output;
    }
}

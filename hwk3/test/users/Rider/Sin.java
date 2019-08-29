import java.math.BigInteger;
import java.util.ArrayList;

public class Sin implements Factor {
    private BigInteger sinExpo;
    
    Sin(String sinExpo) {
        if (new BigInteger(sinExpo).compareTo(
                new BigInteger("10000")) > 0) {
            Wrong.WrongFormat();
        }
        this.sinExpo = new BigInteger(sinExpo);
    }
    
    void setSinExpo(BigInteger sinExpo) {
        this.sinExpo = sinExpo;
    }
    
    BigInteger getSinExpo() {
        return sinExpo;
    }
    
    public ArrayList<Factor> derivate() {
        ArrayList<Factor> derivation = new ArrayList<>();
        BigInteger newCoef = this.sinExpo;
        BigInteger newExpo = this.sinExpo.subtract(BigInteger.ONE);
        derivation.add(new Const(newCoef.toString()));
        derivation.add(new Sin(newExpo.toString()));
        derivation.add(new Cos("1"));
        return derivation;
    }
    
    public StringBuffer print() {
        StringBuffer output = new StringBuffer();
        if (!sinExpo.equals(BigInteger.ZERO)) {
            output.append("sin(x)");
            if (!sinExpo.equals(BigInteger.ONE)) {
                output.append("^");
                output.append(sinExpo);
            }
        } else {
            output.append("1");
        }
        return output;
    }
}

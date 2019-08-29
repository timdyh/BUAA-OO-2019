import java.math.BigInteger;
import java.util.ArrayList;

public class Const implements Factor {
    private BigInteger coef;
    
    Const(String coef) {
        this.coef = new BigInteger(coef);
    }
    
    void setCoef(BigInteger coef) {
        this.coef = coef;
    }
    
    BigInteger getCoef() {
        return coef;
    }
    
    public ArrayList<Factor> derivate() {
        ArrayList<Factor> derivation = new ArrayList<>();
        derivation.add(new Const("0"));
        return derivation;
    }
    
    public StringBuffer print() {
        StringBuffer output = new StringBuffer();
        output.append(coef);
        return output;
    }
    
}

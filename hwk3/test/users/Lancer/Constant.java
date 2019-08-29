import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Constant extends Factor {
    private BigInteger val;

    public Constant(BigInteger val) {
        this.val = val;
    }

    protected ArrayList<Factor> diff() {
        Factor diffRes1 = new Constant(BigInteger.ZERO);

        ArrayList<Factor> diffRes = new ArrayList<>(Arrays.asList(diffRes1));
        return diffRes;
    }

    protected boolean isConst() {
        return true;
    }

    protected BigInteger getConst() {
        return val;
    }

    protected BigInteger getExp() {
        return BigInteger.ONE;
    }

    protected String print() {
        return val.toString();
    }
}
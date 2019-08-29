import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class X extends Factor {
    private BigInteger exp;

    public X(BigInteger exp) {
        if (exp.abs().compareTo(new BigInteger("10000")) > 0) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        this.exp = exp;
    }

    protected ArrayList<Factor> diff() {
        Factor diffRes1 = new Constant(exp);
        Factor diffRes2 = new X(exp.subtract(BigInteger.ONE));

        ArrayList<Factor> diffRes =
                new ArrayList<>(Arrays.asList(diffRes1, diffRes2));
        return diffRes;
    }

    protected boolean isConst() {
        return false;
    }

    protected BigInteger getConst() {
        return BigInteger.ONE;
    }

    protected BigInteger getExp() {
        return exp;
    }

    protected String print() {
        if (exp.equals(BigInteger.ZERO)) {
            return "1";
        } else if (exp.equals(BigInteger.ONE)) {
            return "x";
        } else {
            return "x" + "^" + exp.toString();
        }
    }
}
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class SinX extends Factor {
    private BigInteger exp;

    public SinX(BigInteger exp) {
        if (exp.abs().compareTo(new BigInteger("10000")) > 0) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        this.exp = exp;
    }

    protected ArrayList<Factor> diff() {
        Factor diffRes1 = new Constant(exp);
        Factor diffRes2 = new SinX(exp.subtract(BigInteger.ONE));
        Factor diffRes3 = new CosX(BigInteger.ONE);

        ArrayList<Factor> diffRes =
                new ArrayList<>(Arrays.asList(diffRes1, diffRes2, diffRes3));
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
            return "sin(x)";
        } else {
            return "sin(x)" + "^" + exp.toString();
        }
    }
}
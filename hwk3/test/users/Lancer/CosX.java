import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class CosX extends Factor {
    private BigInteger exp;

    public CosX(BigInteger exp) {
        if (exp.abs().compareTo(new BigInteger("10000")) > 0) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        this.exp = exp;
    }

    protected ArrayList<Factor> diff() {
        Factor diffRes1 = new Constant(BigInteger.ZERO.subtract(exp));
        Factor diffRes2 = new SinX(BigInteger.ONE);
        Factor diffRes3 = new CosX(exp.subtract(BigInteger.ONE));

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
            return "cos(x)";
        } else {
            return "cos(x)" + "^" + exp.toString();
        }
    }
}
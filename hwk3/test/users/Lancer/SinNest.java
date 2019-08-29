import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class SinNest extends Factor {
    private BigInteger exp;
    private Poly nestedPoly;

    public SinNest(BigInteger exp, String nestedPoly) {
        if (exp.abs().compareTo(new BigInteger("10000")) > 0) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        this.exp = exp;
        this.nestedPoly = new Poly(nestedPoly);
    }

    public SinNest(BigInteger exp, Poly nestedPoly) {
        if (exp.abs().compareTo(new BigInteger("10000")) > 0) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        this.exp = exp;
        this.nestedPoly = nestedPoly;
    }

    public ArrayList<Factor> diff() {
        Factor diffRes1 = new Constant(exp);
        Factor diffRes2 = new SinNest(exp.subtract(BigInteger.ONE), nestedPoly);
        Factor diffRes3 = new CosNest(BigInteger.ONE, nestedPoly);
        Factor diffRes4 = nestedPoly.diff().get(0);

        ArrayList<Factor> diffRes =
                new ArrayList<>(Arrays.asList(diffRes1, diffRes2,
                        diffRes3, diffRes4));
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
            return "sin" + "((" + nestedPoly.print() + "))";
        } else {
            return "sin" + "((" + nestedPoly.print() + "))" +
                    "^" + exp.toString();
        }
    }
}
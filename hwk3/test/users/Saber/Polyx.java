import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Polyx implements Father {
    private BigInteger xindex;
    private BigInteger xcoeff = new BigInteger("1");

    Polyx(String line) {
        Pattern p = Pattern.compile("[-\\+]?\\d+");
        Matcher n = p.matcher(line);
        if (n.find()) {
            this.xindex = new BigInteger(n.group());
        } else {
            this.xindex = new BigInteger("1");
        }
    }

    Polyx(BigInteger a, BigInteger b) {
        this.xcoeff = a;
        this.xindex = b;
    }

    public String derivate() {
        BigInteger constant = new BigInteger("1");
        BigInteger coeff = this.xindex;
        BigInteger index = this.xindex.subtract(constant);
        Polyx a = new Polyx(coeff, index);
        return a.print();
    }

    public String print() {
        String a = this.xcoeff.toString() + "*x^" + this.xindex.toString();
        return a;
    }
}


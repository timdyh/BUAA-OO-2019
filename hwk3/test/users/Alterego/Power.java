import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Power extends Factor {
    private BigInteger index = BigInteger.ONE;

    public Power(String str) {
        super(str);
        // System.out.println("@Power "+ str);
        Pattern patternPow = Pattern.compile("x(\\^([-+]?\\d+))?");
        Matcher matcherPow = patternPow.matcher(str);
        if (!matcherPow.matches()) {
            // System.out.println("Power内不匹配");
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        } else {
            if (matcherPow.group(2) != null) {
                index = new BigInteger(matcherPow.group(2));
            }
        }
    }

    public String diff() {
        String result = "";
        if (index.equals(BigInteger.ZERO)) {
            return "0";
        }

        if (index.equals(BigInteger.ONE)) {
            return "1";
        }

        result = index.toString() + "*x";
        if (!index.equals(BigInteger.valueOf(2))) {
            result += "^" + index.subtract(BigInteger.ONE).toString();
        }

        return result;
    }
}

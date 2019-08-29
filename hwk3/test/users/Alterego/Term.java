import java.util.ArrayList;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Term {
    private static String con = "[-+]?\\d+";
    private static String pow = "x(\\^[-+]?\\d+)?";
    private static String sin = "sin\\(.+?\\)(\\^[-+]?\\d+)?";
    private static String cos = "cos\\(.+?\\)(\\^[-+]?\\d+)?";
    private static String expr = "\\(.+?\\)";

    private BigInteger coeff = BigInteger.ONE;
    private ArrayList<Factor> factors = new ArrayList<>();

    public Term(String str) {
        // System.out.println("@Term "+ str);

        int preEndIndex = 0;
        for (int i = 0; i < 2; i++) {
            if (str.charAt(i) == '-') {
                coeff = coeff.negate();
                preEndIndex++;
            } else if (str.charAt(i) == '+') {
                preEndIndex++;
            }
        }

        Pattern patternTerm = Pattern.compile("#(" + con + "|" + pow + "|"
                + sin + "|" + cos + "|" + expr + ")((?=#)|$)");
        Matcher matcherTerm = patternTerm.matcher(str);
        while (matcherTerm.find()) {
            if (matcherTerm.start() != preEndIndex) {
                // System.out.println("Term: 匹配起点非上一次终点");
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            preEndIndex = matcherTerm.end();

            switch (matcherTerm.group(1).charAt(0)) {
                case 'x':
                    factors.add(new Power(matcherTerm.group(1)));
                    break;
                case 's':
                    factors.add(new Sin(matcherTerm.group(1)));
                    break;
                case 'c':
                    factors.add(new Cos(matcherTerm.group(1)));
                    break;
                case '(':
                    factors.add(new Expr(matcherTerm.group(1)));
                    break;
                default:
                    BigInteger bigInt = new BigInteger(matcherTerm.group(1));
                    coeff = coeff.multiply(bigInt);
            }
        }
        if (preEndIndex < str.length()) {
            // System.out.println("Term: 剩余未匹配");
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }

    public String diff() {
        String result = "";
        if (factors.isEmpty() || coeff.equals(BigInteger.ZERO)) {
            return "0";
        }

        for (int i = 0; i < factors.size(); i++) {
            String product = "";
            for (int j = 0; j < factors.size(); j++) {
                if (j == i) {
                    product += "*" + factors.get(j).diff();
                } else {
                    product += "*" + factors.get(j);
                }
            }
            product = product.substring(1);
            result += "+" + product;
        }
        result = result.substring(1);

        if (coeff.equals(BigInteger.ONE)) {
            result = "(" + result + ")";
        } else if (coeff.equals(BigInteger.ONE.negate())) {
            result = "-(" + result + ")";
        } else {
            result = coeff.toString() + "*(" + result + ")";
        }

        return result;
    }
}
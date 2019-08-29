import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Sin extends Factor {
    private static String con = "[-+]?\\d+";
    private static String pow = "x(\\^[-+]?\\d+)?";
    private static String sin = "sin\\(.+?\\)(\\^[-+]?\\d+)?";
    private static String cos = "cos\\(.+?\\)(\\^[-+]?\\d+)?";
    private static String expr = "\\(.+?\\)";

    private BigInteger index = BigInteger.ONE;
    private String var;
    private String base;

    public Sin(String str) {
        super(str);
        // System.out.println("@Sin "+ str);
        Pattern patternSin = Pattern.compile("(sin\\((" + con + "|" + pow + "|"
                + sin + "|" + cos + "|" + expr + ")\\))(\\^([-+]?\\d+))?");
        Matcher matcherSin = patternSin.matcher(str);
        if (!matcherSin.matches()) {
            // System.out.println("Sin内不匹配");
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        var = matcherSin.group(2);
        base = matcherSin.group(1);
        if (matcherSin.group(matcherSin.groupCount()) != null) {
            index = new BigInteger(matcherSin.group(matcherSin.groupCount()));
        }
    }

    public String diff() {
        String result = "";
        Pattern patternX = Pattern.compile("x(\\^\\+?0*1)?");
        Matcher matcherX = patternX.matcher(var);
        if (matcherX.matches()) {
            result = "cos(x)";
        } else {
            result = "cos(" + var + ")*";
            switch (var.charAt(0)) {
                case 'x':
                    result += new Power(var).diff();
                    break;
                case 's':
                    result += new Sin(var).diff();
                    break;
                case 'c':
                    result += new Cos(var).diff();
                    break;
                case '(':
                    result += new Expr(var).diff();
                    break;
                default:
                    return "0";
            }
        }

        if (!index.equals(BigInteger.ONE)) {
            if (!index.equals(BigInteger.valueOf(2))) {
                result = index.toString() + "*" + base
                        + "^" + index.subtract(BigInteger.ONE).toString()
                        + "*" + result;
            } else {
                result = index.toString() + "*" + base + "*" + result;
            }
        }

        return result;
    }
}
import java.util.Scanner;
import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    private static String str;
    private static String result;

    public static void main(String[] args) {
        try {
            str = new Scanner(System.in).nextLine();
            if (isLegal()) {
                Expr expr = new Expr(str);
                result = expr.diff();
                result = result.substring(1, result.length() - 1);
                System.out.println(result);
            } else {
                System.out.println("WRONG FORMAT!");
            }
        } catch (Exception e) {
            // System.out.println("其他错误");
            System.out.println("WRONG FORMAT!");
        }
    }

    private static boolean isLegal() {
        if (str.equals("")) {
            // System.out.println("空串");
            return false;
        }

        if (str.indexOf('#') != -1) {
            // System.out.println("非法字符#");
            return false;
        }

        Pattern patternSpaceInConst =
                Pattern.compile("([-+]\\s*[-+]\\s*[-+]|[*^]\\s*[-+])\\s+\\d+" +
                        "|\\d+\\s+\\d+");
        Matcher matcherSpaceInConst = patternSpaceInConst.matcher(str);
        if (matcherSpaceInConst.find()) {
            // System.out.println("常数内有空白字符");
            return false;
        }

        Pattern patternIndexLimit = Pattern.compile("\\^([-+]?\\d+)");
        Matcher matcherIndexLimit = patternIndexLimit.matcher(str);
        BigInteger index;
        BigInteger max = new BigInteger("10000");
        while (matcherIndexLimit.find()) {
            index = new BigInteger(matcherIndexLimit.group(1));
            if (index.compareTo(max) > 0) {
                // System.out.println("指数绝对值大于10000");
                return false;
            }
        }

        String sin = "s\\s*i\\s*n";
        String cos = "c\\s*o\\s*s";
        Pattern patternTri = Pattern.compile(sin + "|" + cos);
        Matcher matcherTri = patternTri.matcher(str);
        while (matcherTri.find()) {
            if (!matcherTri.group().equals("sin")
                    && !matcherTri.group().equals("cos")) {
                // System.out.println("sin/cos内有空白字符");
                return false;
            }
        }

        str = str.replaceAll("[ \t]+", "");
        if (str.equals("")) {
            // System.out.println("纯空白字符串");
            return false;
        }

        str = "(" + str + ")";

        return true;
    }
}


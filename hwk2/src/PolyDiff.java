import java.math.BigInteger;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PolyDiff {
    private static String str;
    private static StringBuilder sb;

    private static String con = "[-+]?\\d+";
    private static String pow = "x(\\^[-+]?\\d+)?";
    private static String tri = "(sin|cos)\\(x\\)(\\^[-+]?\\d+)?";
    private static String regex =
            "[-+][-+]?(\\*(" + con + "|" + pow + "|" + tri + "))+";

    private static HashMap<String, Term> poly = new HashMap();

    public static void main(String[] args) {
        try {
            str = new Scanner(System.in).nextLine();
            sb = new StringBuilder(str);
        } catch (Exception e) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        if (isLegal()) {
            calculate();
            print();
        } else {
            System.out.println("WRONG FORMAT!");
        }
    }

    private static boolean isLegal() {
        // prejudge
        if (str.equals("")) {
            // System.out.println("空串");
            return false;
        }

        Pattern patternCon =
                Pattern.compile("([-+]\\s*[-+]\\s*[-+]|[*^]\\s*[-+])\\s+\\d+" +
                        "|\\d+\\s+\\d+");
        Matcher matcherCon = patternCon.matcher(str);
        if (matcherCon.find()) {
            // System.out.println("常数内有空白字符");
            return false;
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

        // pretreat
        str = str.replaceAll("[ \t]+", "");
        if (str.equals("")) {
            // System.out.println("纯空白字符串");
            return false;
        }

        if (str.charAt(0) != '-' && str.charAt(0) != '+') {
            str = "+" + str;
        }

        sb = new StringBuilder(str);
        Pattern patternOp = Pattern.compile("(?<![-+*^])[-+]{1,2}");
        Matcher matcherOp = patternOp.matcher(sb);
        int nextStart = 0;
        while (matcherOp.find(nextStart)) {
            nextStart = matcherOp.end();
            sb.insert(nextStart, '*');
            matcherOp = patternOp.matcher(sb);
        }
        str = sb.toString();
        // System.out.println("pretreated: " + str);

        // further judge
        Pattern patternTerm = Pattern.compile(regex);
        Matcher matcherTerm = patternTerm.matcher(str);
        int preEndIndex = 0;
        while (matcherTerm.find()) {
            if (matcherTerm.start() != preEndIndex) {
                // System.out.println("匹配起点非上一次终点");
                return false;
            }
            preEndIndex = matcherTerm.end();
        }
        if (preEndIndex != str.length()) {
            // System.out.println("剩余未匹配");
            return false;
        }

        return true;
    }

    private static void calculate() {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            Term term = new Term(matcher.group());
            if (!term.isZero()) {
                BigInteger k = term.getCoeff();
                BigInteger a = term.getIndexX();
                BigInteger b = term.getIndexSin();
                BigInteger c = term.getIndexCos();

                BigInteger k1 = k.multiply(a);
                BigInteger a1 = a.subtract(BigInteger.ONE);
                BigInteger b1 = b;
                BigInteger c1 = c;
                Term term1 = new Term(k1, a1, b1, c1);

                BigInteger k2 = k.multiply(b);
                BigInteger a2 = a;
                BigInteger b2 = b.subtract(BigInteger.ONE);
                BigInteger c2 = c.add(BigInteger.ONE);
                Term term2 = new Term(k2, a2, b2, c2);

                BigInteger k3 = k.multiply(c).negate();
                BigInteger a3 = a;
                BigInteger b3 = b.add(BigInteger.ONE);
                BigInteger c3 = c.subtract(BigInteger.ONE);
                Term term3 = new Term(k3, a3, b3, c3);

                Term[] terms = {term1, term2, term3};
                String key;			
                Term value;
                for (int i = 0; i < 3; i++) {
                    if (!terms[i].isZero()) {
                        key = terms[i].getIndexX() + "|"
                                + terms[i].getIndexSin() + "|"
                                + terms[i].getIndexCos();
                        if (poly.containsKey(key)) {
                            BigInteger kk1 = poly.get(key).getCoeff();
                            BigInteger kk2 = terms[i].getCoeff();
                            BigInteger kk = kk1.add(kk2);
                            BigInteger aa = terms[i].getIndexX();
                            BigInteger bb = terms[i].getIndexSin();
                            BigInteger cc = terms[i].getIndexCos();
                            value = new Term(kk, aa, bb, cc);
                        } else {
                            value = terms[i];
                        }
                        poly.put(key, value);
                    }
                }
            }
        }
    }

    private static void print() {
        String result = "";
        if (poly.isEmpty()) {
            System.out.println("0");
            return;
        } else {
            Iterator iter = poly.entrySet().iterator();
            Map.Entry<String, Term> entry;
            while (iter.hasNext()) {
                entry = (Map.Entry) iter.next();
                if (entry.getValue().getCoeff().signum() == 1) {
                    result += entry.getValue();
                }
            }
            iter = poly.entrySet().iterator();
            while (iter.hasNext()) {
                entry = (Map.Entry) iter.next();
                if (entry.getValue().getCoeff().signum() == -1) {
                    result += entry.getValue();
                }
            }
        }

        if (result.equals("")) {
            result = "0";
        } else {
            if (result.charAt(0) == '+') {
                result = result.substring(1);
            }
        }
        System.out.println(result);
    }
}
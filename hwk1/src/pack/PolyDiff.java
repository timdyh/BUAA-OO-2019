package pack;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PolyDiff {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        string = pretreat(string);
        if (isLegal(string)) {
            calculate(string);
        } else {
            System.out.println("WRONG FORMAT!");
        }
    }

    public static String pretreat(String s) {
        String string = s;
        int cnt = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '+' || string.charAt(i) == '-') {
                cnt++;
            } else if (string.charAt(i) == ' ' || string.charAt(i) == '\t') {
                continue;
            } else {
                break;
            }
        }
        if (cnt == 0) {
            string = "+" + string;
        }
        return string;
    }

    public static boolean isLegal(String s) {
        if (s.indexOf("#") != -1) {
            return false;
        }

        String string = s;
        String type1 = "[ \t]*[+-][ \t]*[+-]?\\d+[ \t]*\\*[ \t]*x[ \t]*" +
                "(\\^[ \t]*[+-]?\\d+)?[ \t]*";              // 标准型
        String type2 = "[ \t]*[+-][ \t]*[+-]?[ \t]*x[ \t]*" +
                "(\\^[ \t]*[+-]?\\d+)?[ \t]*";              // 无系数
        String type3 = "[ \t]*[+-][ \t]*[+-]?\\d+[ \t]*";   // 常数项
        string = string.replaceAll(type1, "#");
        string = string.replaceAll(type2, "#");
        string = string.replaceAll(type3, "#");
        string = string.replace(" ", "");
        string = string.replace("\t", "");
        string = string.replace("#", "");
        return string.equals("");
    }

    public static void calculate(String s) {
        // 多项式拆分
        Pattern pattern = Pattern.compile("([+-])[ \t]*([+-])?(\\d+)?" +
                "[ \t]*(\\*)?[ \t]*x?[ \t]*(\\^[ \t]*([+-]?\\d+))?");
        Matcher matcher = pattern.matcher(s);
        HashMap<BigInteger, Term> poly = new HashMap();
        while (matcher.find()) {
            Term term = new Term(matcher.group(1), matcher.group(2),
                    matcher.group(3), matcher.group(4), matcher.group(6));
            if (!term.isZero()) {
                if (poly.containsKey(term.getIndex())) {
                    BigInteger a = poly.get(term.getIndex()).getCoeff();
                    BigInteger b = term.getCoeff();
                    BigInteger co = a.add(b);
                    BigInteger in = term.getIndex();
                    poly.put(in, new Term(co, in));
                } else {
                    poly.put(term.getIndex(), term);
                }
            }
        }

        // 求导并打印
        int n = 0;
        Iterator iter = poly.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<BigInteger, Term> entry = (Map.Entry) iter.next();
            Term termDiff = entry.getValue().diff();
            if (!termDiff.isZero()) {
                System.out.print(termDiff);
                n++;
            }
        }
        if (n == 0) {
            System.out.print("0");
        }
        System.out.print("\n");
    }
}
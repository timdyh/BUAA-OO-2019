package poly;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        if (!scan.hasNextLine()) {
            checkFail();
        }
        String string = scan.nextLine();
        checkZero(string);
        checkOne(string);
        string = string.replaceAll("[ \\t]", "");
        checkTwo(string);
        String s = headDeal(string);
        Items items = new Items(s);
        String ss = items.dri();
        ss = simply(ss);
        System.out.print(ss);
    }

    public static void checkFail() {
        System.out.print("WRONG FORMAT!");
        System.exit(0);
    }

    public static void checkZero(String s) {
        if (Pattern.matches("[ \\t]*", s)) {
            checkFail();
        }
    }

    public static void checkOne(String s) {
        Pattern p = Pattern.compile("[^-*+^\\dsincox() \\t]");
        Matcher m = p.matcher(s);
        if (m.find()) {
            checkFail();
        } else {
            Pattern pt = Pattern.compile("[+-][ \\t]*[+-][ \\t]*[+-]" +
                    "[ \\t]+\\d+|\\d+[ \\t]+\\d+|\\^[ \\t]*[+-][ \\t]+" +
                    "\\d+|\\*[ \\t]*[+-][ \\t]+\\d+|s[ \\t]*i[ \\t]*n|" +
                    "c[ \\t]*o[ \\t]*s");
            m = pt.matcher(s);
            while (m.find()) {
                if (m.group().equals("sin") || m.group().equals("cos")) {
                    continue;
                }
                checkFail();
            }
        }
    }

    public static void checkTwo(String s) {
        Pattern p = Pattern.compile("[+-][+-][+-]+(?:x|" +
                "sin[(]x[)]|cos[(]x[)])");
        Matcher m = p.matcher(s);
        if (m.find()) {
            checkFail();
        }
        p = Pattern.compile("\\^[+-][+-]");
        m = p.matcher(s);
        if (m.find()) {
            checkFail();
        }
        p = Pattern.compile("[+-][+-][+-][+-]+");
        m = p.matcher(s);
        if (m.find()) {
            checkFail();
        }
    }

    public static String headDeal(String s) {
        String ss;
        ss = s.replaceAll("\\+\\+\\+?|--\\+?|-\\+-|\\+--", "+");
        ss = ss.replaceAll("---|-\\+|\\+-|-\\+\\+|\\+-\\+|\\+\\+-", "-");
        if (ss.charAt(0) == '*') {
            checkFail();
        } else if (ss.charAt(0) != '+' && ss.charAt(0) != '-') {
            ss = "+" + ss;
        }
        return ss;
    }

    public static String simply(String s) {
        String ss = s.replace("sin(0)", "0");
        ss = ss.replace("1*x^0", "1");
        ss = ss.replace("1*sin(x)^0", "1");
        ss = ss.replace("1*cos(x)^0", "1");
        ss = ss.replace("2*1", "2");
        ss = ss.replace("0*x","0");
        ss = ss.replace("x*0","0");
        ss = ss.replaceAll("0\\*x\\^\\d+", "0");
        ss = ss.replace("cos(x)^0","1");
        ss = ss.replace("sin(x)^0","1");
        ss = ss.replaceAll("0\\^\\d+", "0");
        return ss;
    }
}

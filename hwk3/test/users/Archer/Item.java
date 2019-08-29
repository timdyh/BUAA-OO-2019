package poly;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static poly.Main.checkFail;

public class Item {
    private ArrayList<Func> item = new ArrayList<>();
    private String string = new String();

    public Item() {
    }

    public Item(String s) {
        Pattern[] p = new Pattern[5];
        int sub = 0;
        boolean hasFirst = false;
        p[0] = Pattern.compile("[+-]?[+-]?\\d+");
        p[1] = Pattern.compile("[+-]?x(\\^[+-]?\\d+)?");
        p[2] = Pattern.compile("[+-]?[(]");
        p[3] = Pattern.compile("[+-]?(sin|cos)[(]");
        for (int i = 0; i < 4; i++) {
            Matcher m = p[i].matcher(s);
            if (m.lookingAt()) {
                if (i == 0) {
                    Num n = new Num(m.group());
                    sub = m.group().length();
                    item.add(n);
                    hasFirst = true;
                    break;
                } else if (i == 1) {
                    Power po = new Power(m.group());
                    sub = m.group().length();
                    item.add(po);
                    hasFirst = true;
                    break;
                } else if (i == 2) {
                    if (s.charAt(m.end()) == '^') {
                        checkFail();
                    }
                    Expr ex = new Expr(s.substring(0, rightKo(s) + 1));
                    sub = rightKo(s) + 1;
                    item.add(ex);
                    hasFirst = true;
                    break;
                } else if (i == 3) {
                    int end = rightK(s);
                    Pattern p1 = Pattern.compile("\\^[+-]?\\d+");
                    Matcher m1 = p1.matcher(s.substring(end + 1));
                    if (m1.lookingAt()) {
                        end += m1.end();
                    }
                    Trig tr = new Trig(s.substring(0, end + 1));
                    sub = end + 1;
                    item.add(tr);
                    hasFirst = true;
                    break;
                }
            }
        }
        if (hasFirst == false) {
            checkFail();
        }
        String s1 = s.substring(sub);
        while (!s1.equals("")) {
            if (s1.charAt(0) != '+' && s1.charAt(0) != '-') {
                if (addNum(s1) != 0) {
                    int end = addNum(s1);
                    Num n = new Num(s1.substring(1, end));
                    item.add(n);
                    s1 = s1.substring(end);
                } else if (addPower(s1) != 0) {
                    int end = addPower(s1);
                    Power power = new Power(s1.substring(1, end));
                    item.add(power);
                    s1 = s1.substring(end);
                } else if (addExpr(s1) != 0) {
                    int end = addExpr(s1);
                    Expr ex = new Expr(s1.substring(1, end + 1));
                    item.add(ex);
                    s1 = s1.substring(end + 1);
                } else if (addTrig(s1) != 0) {
                    int end = addTrig(s1);
                    Trig tr = new Trig(s1.substring(1, end + 1));
                    item.add(tr);
                    s1 = s1.substring(end + 1);
                } else {
                    checkFail();
                }
            } else {
                break;
            }
        }
        string = s1;
    }

    public static int addNum(String s) {
        Pattern p = Pattern.compile("\\*[+-]?\\d+");
        Matcher m = p.matcher(s);
        if (m.lookingAt()) {
            return m.end();
        } else {
            return 0;
        }
    }

    public static int addPower(String s) {
        Pattern p = Pattern.compile("\\*x(\\^[+-]?\\d+)?");
        Matcher m = p.matcher(s);
        if (m.lookingAt()) {
            return m.end();
        } else {
            return 0;
        }
    }

    public static int addExpr(String s) {
        Pattern p = Pattern.compile("\\*[(]");
        Matcher m = p.matcher(s);
        if (m.lookingAt()) {
            if (s.charAt(m.end()) == '^') {
                checkFail();
            }
            return rightKo(s);
        } else {
            return 0;
        }
    }

    public static int addTrig(String s) {
        Pattern p = Pattern.compile("\\*(sin|cos)[(]");
        Matcher m = p.matcher(s);
        if (m.lookingAt()) {
            int end = rightK(s);
            Pattern p1 = Pattern.compile("\\^[+-]?\\d+");
            Matcher m1 = p1.matcher(s.substring(end + 1));
            if (m1.lookingAt()) {
                end += m1.end();
            }
            return end;
        } else {
            return 0;
        }
    }

    public static int rightKo(String s) {
        int count = 0;
        int k = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                count++;
            } else if (s.charAt(i) == ')') {
                count--;
            }
            if (count == 0 && i != 0) {
                k = i;
                break;
            }
        }
        if (count != 0) {
            checkFail();
        }
        return k;
    }

    public static int rightK(String s) {
        int count = 0;
        int k = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                count++;
            } else if (s.charAt(i) == ')') {
                count--;
            }
            if (count == 0 && i > 3) {
                k = i;
                break;
            }
        }
        if (count != 0) {
            checkFail();
        }
        return k;
    }

    public String getSt() {
        return string;
    }

    public String dri() {
        String pt = "";
        for (int i = 0; i < item.size(); i++) {
            for (int j = 0; j < item.size(); j++) {
                if (j != 0) {
                    pt = pt + "*";
                }
                if (i == j) {
                    pt = pt + item.get(j).dri();
                } else {
                    pt = pt + item.get(j).print();
                }
            }
        }
        return pt;
    }
}

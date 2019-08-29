package homework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analysis {
    private String str;
    private char ch;
    private int ptr = 0;
    private Rule rule;

    private void nextChar() {
        if (ptr == str.length()) {
            ch = '\0';
        } else {
            ch = str.toCharArray()[ptr++];
        }
    }

    private void error() {
        System.out.println("WRONG FORMAT!");
        System.exit(0);
    }

    private Add expr(int negFlag) {
        Add temp = new Add();
        this.nextChar();
        if (ch != '-' && ch != '+') {
            ptr--;
            temp.add(this.item(negFlag));
        } else if (ch == '-') {
            temp.add(this.item(negFlag * -1));
        } else {
            temp.add(this.item(negFlag));
        }
        this.nextChar();
        while (ch == '+' || ch == '-') {
            if (ch == '-') {
                temp.add(this.item(negFlag * -1));
            } else {
                temp.add(this.item(negFlag));
            }
            this.nextChar();
        }
        if (ch != ')') {
            ptr--;
        }
        return temp;
    }

    private Multi item(int negFlag) {
        Multi temp = new Multi();
        this.nextChar();
        if (ch != '-' && ch != '+') {
            ptr--;
            temp.add(this.factor(negFlag));
        } else if (ch == '-') {
            temp.add(this.factor(negFlag * -1));
        } else {
            temp.add(this.factor(negFlag));
        }
        this.nextChar();
        while (ch == '*') {
            temp.add(this.factor(1));
            this.nextChar();
        }
        ptr--;
        return temp;
    }

    private Nest factor(int negFlag) {
        Nest temp = new Nest();
        String s1 = "^[-+]?\\d+";
        String s2 = "^x(\\^[-+]?\\d+)?";
        String s3 = "(^sin\\()|(^cos\\()";
        String s4 = "^(\\^[-+]?\\d+)?";
        Pattern r1 = Pattern.compile(s1);
        Matcher m1 = r1.matcher(str.substring(ptr));
        if (m1.find()) {
            temp.addOut(new Constant(str.substring(ptr, ptr
                    + m1.end()), negFlag));
            ptr += m1.end();
            return temp;
        }
        Pattern r2 = Pattern.compile(s2);
        Matcher m2 = r2.matcher(str.substring(ptr));
        if (m2.find()) {
            temp.addOut(new Power(str.substring(ptr, ptr + m2.end()), negFlag));
            ptr += m2.end();
            return temp;
        }
        Pattern r3 = Pattern.compile(s3);
        Matcher m3 = r3.matcher(str.substring(ptr));
        if (m3.find()) {
            final int tempPtr = ptr;
            ptr += m3.end();
            temp.addIn(this.factor(1));
            ptr += 1;
            Pattern r4 = Pattern.compile(s4);
            Matcher m4 = r4.matcher(str.substring(ptr));
            if (m4.find()) {
                ptr += m4.end();
            }
            temp.addOut(new Triangle(str.substring(tempPtr, ptr), negFlag));
            return temp;
        }
        String s5 = "^\\(";
        Pattern r5 = Pattern.compile(s5);
        Matcher m5 = r5.matcher(str.substring(ptr));
        if (m5.find()) {
            ptr += m5.end();
            temp.addIn(this.expr(negFlag));
            return temp;
        }
        return temp;
    }

    public Analysis(String poly) {
        str = poly.replaceAll(" ", "");
        str = str.replaceAll("\t", "");
        rule = this.expr(1);
        rule = rule.dev();
        System.out.println(rule.getString());
    }
}

package homework;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
    private String str;
    private char ch;
    private int ptr = 0;

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

    private boolean regDeal(String rule) {
        String s = rule;
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(str.substring(ptr));
        boolean judge = m.find();
        if (!judge) {
            return false;
        } else {
            ptr += m.end();
            return true;
        }
    }

    private void expr() {
        this.regDeal("^[+-]?\\s*");
        this.item();
        while (this.regDeal("^\\s*[-+]\\s*")) {
            this.item();
        }

    }

    private void item() {
        this.regDeal("^[+-]?\\s*");
        this.factor();
        while (this.regDeal("^\\s*\\*\\s*")) {
            this.factor();
        }
    }

    private void factor() {
        if (regDeal("^[-+]?\\d+")) {
            return;
        } else if (regDeal("^x(\\s*\\^\\s*[-+]?\\d+)?")) {
            return;
        } else if (regDeal("^sin\\s*\\(") || regDeal("^cos\\s*\\(")) {
            this.regDeal("^\\s*");
            this.factor();
            this.regDeal("^\\s*");
            this.nextChar();
            if (ch != ')') {
                this.error();
            }
            regDeal("^(\\s*\\^\\s*[-+]?\\d+)?");
            return;
        } else if (regDeal("^\\(\\s*")) {
            this.expr();
            this.regDeal("^\\s*");
            this.nextChar();
            if (ch != ')') {
                this.error();
            }
            return;
        } else {
            this.error();
        }
    }

    public InputHandler() {
        Scanner s = new Scanner(System.in);
        if (s.hasNextLine()) { // 如果有数据输入，则读取一行的多项式，否则报错
            str = s.nextLine();
        } else {
            this.error();
        }
        if (str.contains("\f") || str.contains("\n") // 判断是否有非法空白字符的输入
                || str.contains("\r") || str.contains("\013")) {
            this.error();
        }
        str = str.trim(); // 去掉首和尾的空格
        if (str.equals("")) { // 空串不合法
            this.error();
        }
        while (true) {
            this.expr();
            this.nextChar();
            if (ch == '\0') {
                break;
            } else {
                this.error();
            }
        }
        str = str.trim();
    }

    public String getStr() {
        return str;
    }
}

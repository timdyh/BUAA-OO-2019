import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Found {
    private String result = "";
    private ArrayList term = null;    //每一项
    private String xpoly = "x(\\^[-\\+]?\\d+)?";
    private char[] fuhao = new char[50];
    private int get = 0;
    private int num = 0;

    Found(String line) {
        term = new ArrayList();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '+' || line.charAt(i) == '-') {
                this.symbol(line.charAt(i));
            } else if (line.charAt(i) == '*') {
                if (line.charAt(i) == '*' && line.charAt(i + 1) == '-') {
                    get = 1;
                    this.symbol(line.charAt(i));
                    i++;
                } else {
                    this.symbol(line.charAt(i));
                }
            } else if (line.charAt(i) == 's' || line.charAt(i) == 'c') {
                int flag = 1;
                int start = i;
                for (i = i + 4; flag != 0; i++) {
                    if (line.charAt(i) == '(') {
                        flag++;
                    } else if (line.charAt(i) == ')') {
                        flag--;
                    }
                }
                int end1 = i;
                Pattern a = Pattern.compile("\\^[-\\+]?\\d+");
                Matcher m = a.matcher(line);
                int end2 = end1;
                if (m.find(end1)) {
                    if (m.start() == end1) {
                        end2 = m.end();
                    }
                }
                Complex b = new Complex(line.substring(start, end2));
                term.add(b);
                i = end2 - 1;
            } else if (line.charAt(i) == 'x') {
                Pattern a = Pattern.compile(xpoly);
                Matcher m = a.matcher(line);
                int end = 0;
                if (m.find(i - 1)) {
                    end = m.end();
                }
                Polyx c = new Polyx(line.substring(i, end));
                term.add(c);
                i = end - 1;
            } else if (line.charAt(i) == '(') {
                int flag = 1;
                int start = i;
                for (i++; flag != 0; i++) {
                    if (line.charAt(i) == '(') {
                        flag++;
                    } else if (line.charAt(i) == ')') {
                        flag--;
                    }
                }
                int end = i;
                String a = line.substring(start, end);
                Complex b = new Complex(a);
                term.add(b);
                i = end - 1;
            } else {
                int start = i;
                for (; line.charAt(i) >= '0' && line.charAt(i) <= '9'; i++) {
                    if (i == line.length() - 1) {
                        i++;
                        break;
                    }
                }
                int end = i;
                String normal = line.substring(start, end);
                if (get == 1) {
                    get = 0;
                    normal = "-" + normal;
                }
                Constant a = new Constant(normal);
                term.add(a);
                i = end - 1;
            }
        }
        String other = this.next();
        result = result + other;
        result = result.replaceAll("-\\+\\+|\\+\\+-|\\+-\\+|---", "-");
        result = result.replaceAll("\\+\\+\\+|\\+--|--\\+|-\\+-", "\\+");
        result = result.replaceAll("\\+-|-\\+", "-");
        result = result.replaceAll("--|\\+\\+", "\\+");
    }

    public void symbol(char a) {
        if (num == 0 || a == '*') {
            fuhao[num++] = a;
        } else {
            if (fuhao[num - 1] == '*') {
                String b = push();
                result = result + "+" + b;

            }
            fuhao[num++] = a;
        }
    }

    public String push() {
        int i;
        int number = 0;
        for (i = num - 1; i >= 0; i--) {
            if (fuhao[i] == '*') {
                number++;
            } else {
                break;
            }
        }
        number++;
        num = i;
        Father[] other = new Father[number];
        int j = 0;
        for (i = term.size() - 1; j < number; i--, j++) {
            Father a = (Father) term.get(i);
            other[j] = a;
            term.remove(i);
        }
        String string = "";
        for (i = 0; i < number; i++) {
            String part = other[i].derivate();
            for (j = 0; j < number; j++) {
                if (i != j) {
                    part = part + "*" + other[j].print();
                }
            }
            string = string + String.valueOf(fuhao[num]) + part;
        }
        return string;
    }

    public String next() {
        String result1 = "";
        if (num == 0) {
            return result1;
        } else {
            if (fuhao[num - 1] == '*') {
                String b = push();
                result = result + "+" + b;
            }
            while (num > 0) {
                Father a = (Father) term.get(term.size() - 1);
                term.remove(term.size() - 1);
                result1 = result1 + String.valueOf(fuhao[--num]) + a.derivate();
            }
            return result1;
        }
    }

    public String getResult() {
        return this.result;
    }
}

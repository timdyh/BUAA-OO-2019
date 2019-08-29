import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Complex implements Father {
    private String exp;
    private BigInteger one = new BigInteger("1");
    private BigInteger zero = new BigInteger("0");
    private BigInteger sindex;
    private BigInteger cindex;
    private BigInteger coeff;

    Complex(String line) {
        this.exp = line;
    }

    public String add(String line) {
        String other = line;
        if (other.charAt(0) != '+' && other.charAt(0) != '-') {
            other = "+" + other;
        }
        return other;
    }

    public String derivate() {
        Pattern x = Pattern.compile("x");
        Matcher m = x.matcher(this.exp);
        if (!m.find()) {
            Complex a = new Complex("0");
            return a.print();
        } else {
            String result = "";
            int exlen = exp.length();
            if (this.exp.charAt(0) == '(') {
                String a = this.exp.substring(1, exlen);//截取括号里面的东西
                a = a.substring(0, a.length() - 1);
                a = this.add(a);
                Found b = new Found(a);
                result = "(" + b.getResult() + ")";
            } else if (this.exp.charAt(0) == 'c') {
                result = this.derivation2();
            } else {
                result = this.derivation1();
            }
            return result;
        }
    }

    public String derivation1() {
        String result = "";
        String line1;
        String line2;
        String line3;
        int exle = this.exp.length();
        if (exp.charAt(exp.length() - 1) >= '0' &&
                exp.charAt(exp.length() - 1) <= '9') {
            int i = this.get();
            String index = exp.substring(i + 1, exle);
            sindex = new BigInteger(index);
            coeff = sindex;
            line1 = exp.substring(0, i);
            line1 = line1.substring(4, line1.length());
            line3 = "sin(" + line1;
            sindex = sindex.subtract(one);
            result = result + coeff.toString() + "*";
            if (!sindex.equals(zero)) {
                if (sindex.equals(one)) {
                    result = result + line3 + "*";
                } else {
                    result = result + line3 + "^" + sindex.toString() + "*";
                }
            }
            line2 = "cos(" + line1;
            result = result + line2;
            line1 = line1.substring(0, line1.length() - 1);
            line1 = this.add(line1);
            Found b = new Found(line1);
            result = result + "*" + b.getResult();
        } else {
            line1 = exp.substring(4, this.exp.length());
            line1 = line1.substring(0, line1.length() - 1);
            line2 = "cos(" + line1 + ")";
            line1 = this.add(line1);
            Found b = new Found(line1);
            result = result + line2 + "*" + b.getResult();
        }
        return result;
    }

    public String derivation2() {
        String result = "";
        String line1;
        String line2;
        String line3;
        if (this.exp.charAt(this.exp.length() - 1) == ')') {  //没有幂
            line1 = this.exp.substring(4, exp.length());
            line1 = line1.substring(0, line1.length() - 1);
            line2 = "sin(" + line1 + ")";
            line1 = this.add(line1);
            Found b = new Found(line1);
            result = result + "-1*" + line2 + "*" + b.getResult();
        } else {
            int i = this.get();
            String index = this.exp.substring(i + 1, this.exp.length());
            cindex = new BigInteger(index);
            coeff = cindex.negate();
            line1 = this.exp.substring(0, i);
            line1 = line1.substring(4, line1.length());
            line2 = "cos(" + line1;
            cindex = cindex.subtract(one);
            result = result + coeff.toString() + "*";
            if (!cindex.equals(zero)) {
                if (cindex.equals(one)) {
                    result = result + line2 + "*";
                } else {
                    result = result + line2 + "^" + cindex.toString() + "*";
                }
            }
            line3 = "sin(" + line1;
            result = result + line3;
            line1 = line1.substring(0, line1.length() - 1);
            line1 = this.add(line1);
            Found b = new Found(line1);
            result = result + "*" + b.getResult();
        }
        return result;
    }

    public int get() {
        int i = this.exp.length();
        for (i = i - 1; this.exp.charAt(i) != '^'; ) {
            i--;
        }
        return i;
    }

    public String print() {  //输出的时候就返回其本身即可
        return this.exp;
    }
}

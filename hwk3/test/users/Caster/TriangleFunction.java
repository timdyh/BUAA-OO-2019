package derivation;

import java.util.Stack;
import java.util.regex.Pattern;

class TriangleFunction extends Divisor {
    private static final String COS = "^cos\\(.+\\)(\\^\\d+)?$";
    private static final String SIN = "^sin\\(.+\\)(\\^\\d+)?$";
    private static final String X = "^x$";
    private static final String CON = "^[+-]?\\d+$";
    private static final String POLY = "^\\(.+\\)$";
    private static final String XTERM = "^x(\\^\\d+)?$";
    private PolyTree polyTree;
    private Integer id;
    /* mark whether cos or sin
        and -1 for cos 1 for sin
    */

    TriangleFunction(String input) throws  Exception {
        super(input);
    }

    @Override
    void MergeDivisor() throws Exception {
        if (Pattern.compile(COS).matcher(super.GetDivisor()).matches()) {
            this.id = -1;
        } else if (Pattern.compile(SIN).matcher(super.GetDivisor()).matches()) {
            this.id = 1;
        }
        Stack<CharAndLocation> stackOfDivisor = new Stack<>();
        StringBuilder var = new StringBuilder(super.GetDivisor());
        SplitString splitString = new SplitString();
        for (int i = 0;i < var.length();i++) {
            if (var.charAt(i) == '('
                    || var.charAt(i) == '^') {
                stackOfDivisor.push(
                        new CharAndLocation(i,var.charAt(i)));
            } else {
                stackOfDivisor = splitString.
                        CleanStack(var,stackOfDivisor,i);
            }
        }
        String[] strings = splitString.Split(stackOfDivisor,var);
        if (strings.length == 1) {
            super.ChangePower(1);
        } else {
            super.ChangePower(Integer.parseInt(strings[1]));
        }
        var =  new StringBuilder(strings[0]);
        String power = String.valueOf(super.GetPower());
        super.ChangeDivisor(var + "^" + power);
        var.delete(0,4);
        int len = var.length();
        var.deleteCharAt(len - 1);
        Pattern pattern1 = Pattern.compile(COS);
        Pattern pattern2 = Pattern.compile(SIN);
        Pattern pattern3 = Pattern.compile(CON);
        Pattern pattern4 = Pattern.compile(POLY);
        Pattern pattern5 = Pattern.compile(XTERM);
        if (!pattern1.matcher(var).matches() &&
            !pattern2.matcher(var).matches() &&
            !pattern3.matcher(var).matches() &&
            !pattern4.matcher(var).matches() &&
            !pattern5.matcher(var).matches()) {
            throw new Exception("WRONG FORMAT!");
        }
        super.ChangeBase(var.toString());
        polyTree = new PolyTree(var.toString());
        polyTree.BuildPolyTree();
    }

    @Override
    String Derivation() {
        String derivation = "";
        String power;
        power = Integer.toString(super.GetPower() - 1);
        String coe;
        coe = Integer.toString(super.GetPower());
        String base = super.GetBse();
        if (Pattern.compile(X).matcher(base).matches()) {
            if (this.id == 1) {
                if (coe.equals("1")) {
                    return "cos(x)";
                } else {
                    return coe + "*sin(x)^" + power + "*cos(x)";
                }
            } else if (this.id == -1) {
                if (coe.equals("1")) {
                    return "-1*sin(x)";
                } else {
                    return coe + "*cos(x)^" + power + "*-1*sin(x)";
                }
            }
        } else if (Pattern.compile(CON).matcher(base).matches()) {
            return "0";
        } else if (super.GetPower() != 1) {
            power = "^" + power;
            if (this.id == 1) {
                derivation = derivation.concat(coe +
                        "*" + "sin(" + base + ")" + power + "*cos("
                        + base + ")*");
            } else if (this.id == -1) {
                derivation = derivation.concat(coe +
                        "*" + "cos(" + base + ")" + power + "*-1*sin("
                        + base + ")*");
            }
        } else if (super.GetPower() == 1) {
            if (this.id == 1) {
                derivation = "cos(" + base + ")*";
            } else {
                derivation =  "-1*sin(" + base + ")*";
            }
        }
        return derivation + super.PolyTreeDer(this.polyTree);
    }
}

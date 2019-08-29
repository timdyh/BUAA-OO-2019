package derivation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;

class Term {
    private static final String X = "^x(\\^\\d+)?$";
    private static final String CON = "^[+-]?\\d+$";
    private static final String TRI = "^((cos)|(sin))\\(.+\\)(\\^\\d+)?$";
    private static final String POLY = "^\\(.+\\)$";
    private ArrayList<Divisor> divisors = new ArrayList<>();
    private String term;
    private Character op;

    Term(String input,Character op) {
        this.term = input;
        this.op = op;

    }

    void BuildDivisorTree() throws Exception {
        Stack<CharAndLocation> stackOfTerm = new Stack<>();
        StringBuilder stringBuilder = new StringBuilder(this.term);
        SplitString splitString = new SplitString();
        for (int i = 0;i < stringBuilder.length();i++) {
            if (stringBuilder.charAt(i) == '('
                    || stringBuilder.charAt(i) == '*') {
                stackOfTerm.push(
                        new CharAndLocation(i,stringBuilder.charAt(i)));
            } else {
                stackOfTerm = splitString.
                        CleanStack(stringBuilder,stackOfTerm,i);
            }
        }
        String[] termString = splitString.Split(stackOfTerm,stringBuilder);
        for (String string: termString
        ) {
            String body = string.replaceAll(" ","");
            if (Pattern.compile(X).matcher(body).matches()) {
                PowerFunction divisor =  new PowerFunction(string);
                this.divisors.add(divisor);
            } else if (Pattern.compile(CON).matcher(body).matches()) {
                Constants divisor = new Constants(string);
                this.divisors.add(divisor);
            } else if (Pattern.compile(TRI).matcher(body).matches()) {
                TriangleFunction divisor = new TriangleFunction(string);
                this.divisors.add(divisor);
            } else if (Pattern.compile(POLY).matcher(body).matches()) {
                PolyDivisor divisor = new PolyDivisor(string);
                this.divisors.add(divisor);
            } else {
                throw new Exception("WRONG FORMAT!");
            }
        }
        MergeDivisor();
    }

    private void MergeDivisor() throws Exception {
        BigInteger newPower = BigInteger.ZERO;
        for (int i = 0;i < divisors.size();) {
            if (divisors.get(i) instanceof PowerFunction) {
                newPower = newPower.add(
                        new BigInteger(divisors.get(i).GetPower().toString()));
                divisors.remove(i);
            } else {
                i++;
            }
        }
        if (newPower.compareTo(BigInteger.ZERO) != 0) {
            divisors.add(new PowerFunction("x^" + newPower.toString()));
        }
        BigInteger newCon = new BigInteger("1");
        for (int i = 0;i < divisors.size();) {
            if (divisors.get(i) instanceof Constants) {
                newCon = newCon.multiply(
                        new BigInteger(divisors.get(i).GetDivisor()));
                divisors.remove(i);
            } else {
                i++;
            }
        }
        divisors.add(new Constants(newCon.toString()));
    }

    String Derivation() {
        ArrayList<String> strings = new ArrayList<>();
        if (Pattern.compile("[+-]?\\d+$").matcher(term).matches()) {
            return "0";
        } else if (Pattern.compile("^\\+?x$").matcher(term).matches()) {
            return "1";
        }
        for (int i = 0;i < this.divisors.size();i++) {
            String derivation = "";
            if (divisors.get(i) instanceof Constants && divisors.size() > 1) {
                continue;
            }
            derivation = derivation + this.divisors.get(i).Derivation();
            for (int j = 0;j < this.divisors.size();j++) {
                if (j != i) {
                    derivation = derivation.concat("*" +
                            this.divisors.get(j).GetDivisor());
                }
            }
            if (divisors.size() >= 2 && i != this.divisors.size() - 2)  {
                derivation = derivation + "+";
            }
            strings.add(derivation);
        }
        String result = "(";
        for (String string: strings
             ) {
            result = result.concat(string);
        }
        result = result + ")";
        return result;
    }

    String GetOp() {
        return this.op.toString();
    }

    String GetTerm() {
        return this.term;
    }
}

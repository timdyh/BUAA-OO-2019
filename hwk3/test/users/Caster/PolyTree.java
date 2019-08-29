package derivation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PolyTree {
    private ArrayList<Term> terms = new ArrayList<>();
    private String input;

    PolyTree(String input) throws Exception {
        this.input = input;
        InitInput();
    }

    private void InitInput() throws Exception { // some pre-process
        String output;
        String illegalSpace = "(\\d[ \t]+\\d)|(o[ \t]+s)|(c[ \t]+o)" +
                "|(s[ \t]+i)|(i[ \t]+n)|([)][ \t]*\\d)|([)][ \t]*[csx])" +
                "(x[ \t]*[sc\\d])|(\\d[ \t]*[xsc])" +
                "|([ \t]*[+-][ \t]*[+-][ \t]*[+-][ \t]+)" +
                "|([ \t]*[+-][ \t]*[+-][ \t]*[+-][ \t]*[(xcs])" +
                "|(\\^[ \t]*[+-]([ \t]*[+-])+)" +
                "|(\\d[ \t]*\\^[ \t]*\\d)|(\\*[ \t]*[+-]([ \t]*[+-])+)";
        Pattern  pattern = Pattern.compile(illegalSpace);
        Matcher matcher = pattern.matcher(input);
        output = input.replaceAll("[ \t]","");
        String illegalChar = "^[sincox\\d()^*+-]+$";
        Pattern pattern1 = Pattern.compile(illegalChar);
        Matcher matcher1 = pattern1.matcher(output);
        if (matcher.find() || !matcher1.matches() || output.length() == 0) {
            throw new Exception("WRONG FORMAT!");
        }
        output = output.replaceAll("(\\+){2,3}","+");
        output = output.replaceAll("-{3}","-");
        output = output.replaceAll("((\\+)?-{2})|(-{2}(\\+)?)","+");
        output = output.replaceAll("(\\+{1,2}-)|(-\\+{1,2})","-");
        StringBuilder stringBuilder = new StringBuilder(output);
        output = stringBuilder.insert(0,'+').toString();
        output = output.replaceAll("(\\+){2}","+");
        output = output.replaceAll("(\\+-)|(-\\+)","-");
        output = output.replaceAll("\\^\\+","^");
        String power = "\\^\\d+";
        Pattern pattern2 = Pattern.compile(power);
        Matcher matcher2 = pattern2.matcher(output);
        while (matcher2.find()) {
            Pattern pattern3 = Pattern.compile("\\d+");
            Matcher matcher3 = pattern3.matcher(matcher2.group());
            if (matcher3.find()) {
                BigInteger bigInteger = new BigInteger(matcher3.group());
                if (bigInteger.abs().compareTo(new BigInteger("10000")) > 0) {
                    throw new Exception("WRONG FORMAT!");
                }
            }
        }
        this.input = output;
    }

    void BuildPolyTree() throws Exception {
        Stack<CharAndLocation> stackOfPoly = new Stack<>();
        StringBuilder stringBuilder = new StringBuilder(this.input);
        int len = stringBuilder.length();
        SplitString splitString = new SplitString();
        for (int i = 0;i < len;i++) {
            if (stringBuilder.charAt(i) == '('
                    || (stringBuilder.charAt(i) == '+' &&
                    (i == 0 || (stringBuilder.charAt(i - 1) != '*')))
                    || (stringBuilder.charAt(i) == '-' && (
                            i == 0 || (stringBuilder.charAt(i - 1) != '*')
                    ))) {
                stackOfPoly.push(
                        new CharAndLocation(i,stringBuilder.charAt(i)));
            } else {
                stackOfPoly = splitString.
                        CleanStack(stringBuilder,stackOfPoly,i);
            }
        }
        String[] termString = splitString.Split(stackOfPoly,stringBuilder);
        int i = 0;
        for (String string: termString
             ) {
            Term term = new Term(string,stackOfPoly.get(i).GetCharacter());
            this.terms.add(term);
            i++;
        }
        for (Term term: terms
             ) {
            term.BuildDivisorTree();
        }
    }

    ArrayList<String> Derivation() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0;i < this.terms.size();i++) {
            if (i == 0 && this.terms.get(i).GetOp().equals("+")) {
                strings.add(terms.get(i).Derivation());
            } else if (!Pattern.compile("^[+-]?\\d+$").
                    matcher(terms.get(i).GetTerm()).matches() || i == 0) {
                strings.add(terms.get(i).GetOp() + terms.get(i).Derivation());
            }
        }
        return strings;
    }

    String Simplify(String ori) {
        String newOri = ori;
        newOri = newOri.replaceAll("\\^1\\)",")");
        newOri = newOri.replaceAll("\\^1\\+","+");
        newOri = newOri.replaceAll("\\^1\\*","*");
        newOri = newOri.replaceAll("\\^1-","-");
        newOri = newOri.replaceAll("\\*1\\*","*");
        newOri = newOri.replaceAll("\\*1\\)",")");
        newOri = newOri.replaceAll("\\*1\\+","+");
        newOri = newOri.replaceAll("\\*1-","-");
        StringBuilder stringBuilder = new StringBuilder(newOri);
        if (stringBuilder.charAt(0) == '+') {
            stringBuilder.deleteCharAt(0);
        }
        return stringBuilder.toString();
    }

    String PrintTree(ArrayList<String> terms) {
        String result = "";
        for (String string: terms
             ) {
            result = result.concat(string);
        }
        return result;
    }

    String GetInput() {
        return this.input;
    }
}

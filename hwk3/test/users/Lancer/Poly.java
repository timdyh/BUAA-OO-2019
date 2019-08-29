import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Poly extends Factor {
    private ArrayList<Term> terms;

    public Poly(String str) {
        //check str validity
        if (str.endsWith("+") ||
                str.endsWith("-") ||
                str.endsWith("*")) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }

        String[] terms = getTerms(str);
        this.terms = new ArrayList<>();
        for (int i = 0; i < terms.length; i++) {
            Term term = new Term(terms[i]);
            this.terms.add(term);
        }
    }

    public Poly() {
        terms = new ArrayList<>();
    }

    protected String[] getTerms(String str) {
        String poly = str;
        if (poly.charAt(0) == '+') {
            poly = poly.substring(1);
        }

        //store contents between parentheses on top layer into nests
        // and replace it with $
        ArrayList<String> nests = new ArrayList<>();
        Stack stack = new Stack();
        int start = 0;
        int end;
        for (int i = 0; i < poly.length(); i++) {
            if (poly.charAt(i) == '(') {
                if (stack.empty()) {
                    start = i;
                }
                stack.push(poly.charAt(i));
            } else if (poly.charAt(i) == ')') {
                if (stack.empty()) {
                    System.out.println("WRONG FORMAT!");
                    System.exit(0);
                }
                stack.pop();
                if (stack.empty()) {
                    end = i;
                    if (end == start + 1) {
                        System.out.println("WRONG FORMAT!");
                        System.exit(0);
                    }

                    nests.add(poly.substring(start + 1, end));
                    poly = poly.substring(0, start + 1) +
                            "$" +
                            poly.substring(end);
                    i = start + 2;
                }
            }
        }

        //add "+" in front of coe "-"
        for (int i = 1; i < poly.length(); i++) {
            if (poly.charAt(i) == '-' &&
                    poly.charAt(i - 1) != '^' &&
                    poly.charAt(i - 1) != '*' &&
                    poly.charAt(i - 1) != '+') {
                poly = poly.substring(0, i) + "+" + poly.substring(i);
            }
        }

        //split by "+"
        String[] terms = poly.split("\\+");

        //restore $
        for (int i = 0; i < terms.length; i++) {
            for (int j = 0; j < terms[i].length(); j++) {
                if (terms[i].charAt(j) == '$') {
                    terms[i] = terms[i].substring(0, j) +
                            nests.get(0) +
                            terms[i].substring(j + 1);
                    nests.remove(0);
                }
            }
        }
        return terms;
    }

    protected ArrayList<Factor> diff() {
        Factor diffRes1 = new Poly();
        for (int i = 0; i < terms.size(); i++) {
            ((Poly) diffRes1).terms.addAll(terms.get(i).diff());
        }

        ArrayList<Factor> diffRes = new ArrayList<>(Arrays.asList(diffRes1));
        return diffRes;
    }

    protected boolean isConst() {
        return false;
    }

    protected BigInteger getConst() {
        return BigInteger.ONE;
    }

    protected BigInteger getExp() {
        return BigInteger.ONE;
    }

    protected String print() {
        String str = "";

        if (terms.size() == 1) {
            str += terms.get(0).print();
            return str;
        }

        str += "(";
        for (int i = 0; i < terms.size(); i++) {
            str += "+" + terms.get(i).print();
        }
        str += ")";

        if (str.matches("\\(\\)")) {
            str = "";
        } else if (str.charAt(1) == '+') {
            str = "(" + str.substring(2);
        }
        return str;
    }
}
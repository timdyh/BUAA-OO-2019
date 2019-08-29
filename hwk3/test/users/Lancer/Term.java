import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;

public class Term {
    private ArrayList<Factor> factors;

    public Term(String str) {
        String term = str;

        String numReg = "[+-]?\\d+";
        String xxReg = "x";
        String sinReg = "sin\\(x\\)";
        String cosReg = "cos\\(x\\)";

        //store contents between parentheses on top layer into nests
        // and replace it with $
        ArrayList<String> nests = new ArrayList<>();
        Stack stack = new Stack();
        int start = 0;
        int end;
        for (int i = 0; i < term.length(); i++) {
            if (term.charAt(i) == '(') {
                if (stack.empty()) {
                    start = i;
                }
                stack.push(term.charAt(i));
            } else if (term.charAt(i) == ')') {
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

                    nests.add(term.substring(start + 1, end));
                    term = term.substring(0, start + 1) +
                            "$" +
                            term.substring(end);
                    i = start + 2;
                }
            }
        }

        String[] factors = term.split("\\*");
        //check validity
        for (int i = 0; i < factors.length; i++) {
            if (factors[i].length() == 0) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
        }

        this.factors = new ArrayList<>();
        for (int i = 0; i < factors.length; i++) {
            String factor = factors[i];
            if (factor.matches(numReg)) {
                Factor cons = new Constant(new BigInteger(factors[i]));
                this.factors.add(cons);
                continue;
            }

            if (factor.charAt(0) == '-') {
                BigInteger miO = BigInteger.ZERO.subtract(BigInteger.ONE);
                Factor minusOne = new Constant(miO);
                this.factors.add(minusOne);

                factor = factor.substring(1);
            }

            //check validity
            if (factor.length() == 0) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }

            if (factor.charAt(0) == '(' &&
                    factor.charAt(factor.length() - 1) == ')') {
                //restore $
                for (int j = 0; j < factor.length(); j++) {
                    if (factor.charAt(j) == '$') {
                        factor = factor.substring(0, j) +
                                nests.get(0) +
                                factor.substring(j + 1);
                        nests.remove(0);
                    }
                }

                Factor poly =
                        new Poly(factor.substring(1, factor.length() - 1));
                this.factors.add(poly);
                continue;
            }

            int expInd = factor.indexOf('^');
            BigInteger exp;
            if (expInd != -1) {
                String exper = factor.substring(expInd + 1);
                if (!exper.matches(numReg)) {
                    System.out.println("WRONG FORMAT!");
                    System.exit(0);
                }
                exp = new BigInteger(factor.substring(expInd + 1));
                factor = factor.substring(0, expInd);
            } else {
                exp = BigInteger.ONE;
            }

            if (factor.matches(xxReg)) {
                Factor xx = new X(exp);
                this.factors.add(xx);
                continue;
            }

            if (nests.size() == 0) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }

            //restore $
            start = 0;
            end = 0;
            for (int j = 0; j < factor.length(); j++) {
                if (factor.charAt(j) == '$') {
                    factor = factor.substring(0, j) +
                            nests.get(0) +
                            factor.substring(j + 1);
                    nests.remove(0);
                }
            }
            if (factor.matches(sinReg)) {
                Factor sinX = new SinX(exp);
                this.factors.add(sinX);
            } else if (factor.matches(cosReg)) {
                Factor cosX = new CosX(exp);
                this.factors.add(cosX);
            } else {
                if (factor.startsWith("sin(") &&
                        factor.endsWith(")")) {
                    String nestedPoly =
                            factor.substring(4,
                                    factor.length() - 1);
                    String factorTest = nestedPoly;

                    //check if nestedPoly is a num
                    if (nestedPoly.matches(numReg)) {
                        SinNest sinNest = new SinNest(exp, nestedPoly);
                        this.factors.add(sinNest);
                        continue;
                    }

                    //check if nestedPoly is a factor
                    stack = new Stack();
                    for (int j = 0; j < factorTest.length(); j++) {
                        if (factorTest.charAt(j) == '(') {
                            if (stack.empty()) {
                                start = j;
                            }
                            stack.push(factorTest.charAt(i));
                        } else if (factorTest.charAt(j) == ')') {
                            if (stack.empty()) {
                                System.out.println("WRONG FORMAT!");
                                System.exit(0);
                            }
                            stack.pop();
                            if (stack.empty()) {
                                end = j;
                                factorTest =
                                        factorTest.substring(0,
                                                start + 1) +
                                                "$" +
                                                factorTest.substring(end);
                                j = start + 2;
                            }
                        }
                    }
                    if (factorTest.indexOf('*') != -1 ||
                            factorTest.indexOf('+') != -1 ||
                            factorTest.indexOf('-') != -1) {
                        System.out.println("WRONG FORMAT!");
                        System.exit(0);
                    }
                    Factor sinNest = new SinNest(exp, nestedPoly);
                    this.factors.add(sinNest);
                } else if (factor.startsWith("cos(") &&
                        factor.endsWith(")")) {
                    String nestedPoly =
                            factor.substring(4,
                                    factor.length() - 1);
                    String factorTest = nestedPoly;

                    //check if nestedPoly is num
                    if (nestedPoly.matches(numReg)) {
                        CosNest cosNest = new CosNest(exp, nestedPoly);
                        this.factors.add(cosNest);
                        continue;
                    }

                    //check if nestedPoly is a factor
                    stack = new Stack();
                    for (int j = 0; j < factorTest.length(); j++) {
                        if (factorTest.charAt(j) == '(') {
                            if (stack.empty()) {
                                start = j;
                            }
                            stack.push(factorTest.charAt(i));
                        } else if (factorTest.charAt(j) == ')') {
                            if (stack.empty()) {
                                System.out.println("WRONG FORMAT!");
                                System.exit(0);
                            }
                            stack.pop();
                            if (stack.empty()) {
                                end = j;
                                factorTest =
                                        factorTest.substring(0,
                                                start + 1) +
                                                "$" +
                                                factorTest.substring(end);
                                j = start + 2;
                            }
                        }
                    }
                    if (factorTest.indexOf('*') != -1 ||
                            factorTest.indexOf('+') != -1 ||
                            factorTest.indexOf('-') != -1) {
                        System.out.println("WRONG FORMAT!");
                        System.exit(0);
                    }

                    Factor cosNest = new CosNest(exp, nestedPoly);
                    this.factors.add(cosNest);
                } else {
                    System.out.println("WRONG FORMAT!");
                    System.exit(0);
                }
            }
        }
    }

    public Term(ArrayList<Factor> factors) {
        this.factors = factors;
    }

    protected ArrayList<Term> diff() {
        ArrayList<Term> diffRes = new ArrayList<>();
        for (int i = 0; i < this.factors.size(); i++) {
            ArrayList<Factor> factors = new ArrayList<>();
            for (int j = 0; j < this.factors.size(); j++) {
                factors.add(this.factors.get(j));
            }

            ArrayList<Factor> factorDiffRes = factors.get(i).diff();
            factors.remove(i);
            factors.addAll(factorDiffRes);

            Term termDiffRes = new Term(factors);
            diffRes.add(termDiffRes);
        }
        return diffRes;
    }

    protected void mergeConst() {
        BigInteger coe = BigInteger.ONE;
        for (int i = 0; i < factors.size(); i++) {
            coe = coe.multiply(factors.get(i).getConst());
            if (factors.get(i).isConst()) {
                factors.remove(i);
                i--;
            } else if (factors.get(i).getExp().equals(BigInteger.ZERO)) {
                factors.remove(i);
                i--;
            }
        }

        if (factors.size() != 0 && coe.equals(BigInteger.ONE)) {
            return;
        }

        Constant termCoe = new Constant(coe);
        factors.add(0, termCoe);
        return;
    }

    protected String print() {
        mergeConst();
        String str = "";

        //print the first factor
        str += factors.get(0).print();

        //print the rest factors
        for (int i = 1; i < factors.size(); i++) {
            str += "*" + factors.get(i).print();
        }
        return str;
    }
}
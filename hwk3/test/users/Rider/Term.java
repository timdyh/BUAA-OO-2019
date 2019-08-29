import java.math.BigInteger;
import java.util.ArrayList;

class Term {
    private String term;
    private ArrayList<Factor> factors = new ArrayList<>();
    private ArrayList<String> factorsString = new ArrayList<>();
    private String constant = "[+-]?\\d+";
    private String sin = "sin\\(x\\)(\\^[+-]?\\d+)?";
    private String cos = "cos\\(x\\)(\\^[+-]?\\d+)?";
    private String xpow = "x(\\^[+-]?\\d+)?";
    private String poly = "\\((.*)\\)";
    private String composite = "(sin|cos)\\((.*)\\)(\\^[+-]?\\d+)?";
    private Xpow xpowFactor;
    private Sin sinFactor;
    private Cos cosFactor;
    private Const constFactor;
    private boolean coefPositive;
    
    Term(String term,boolean termPositive) {
        this.term = term;
        /*
        factors.add(xpowFactor);
        factors.add(sinFactor);
        factors.add(cosFactor);
        if (termPositive) {
            constFactor = new Const("1");
        } else {
            constFactor = new Const("-1");
        }
        factors.add(constFactor);*/
        coefPositive = termPositive;
        splitFactors();
        addFactors();
    }
    
    Term(ArrayList<Factor> factors) {
        this.factors = factors;
    }
    
    void splitFactors() {
        String termTemp = term + "*";
        if (termTemp.charAt(0) == '+') {
            termTemp = termTemp.substring(1);
        } else if (termTemp.charAt(0) == '-') {
            coefPositive = !coefPositive;
            termTemp = termTemp.substring(1);
        }
        int bracketCount = 0;
        int i;
        int start;
        for (i = 0;i < termTemp.length();i++) {
            start = i;
            for (; i < termTemp.length(); i++) {
                if (termTemp.charAt(i) == '(') {
                    bracketCount++;
                } else if (termTemp.charAt(i) == ')') {
                    bracketCount--;
                } else if (termTemp.charAt(i) == '*') {
                    if (bracketCount == 0) {
                        factorsString.add(termTemp.substring(start, i));
                        break;
                    }
                }
                if (i == termTemp.length() - 2 && bracketCount != 0) {
                    Wrong.WrongFormat();
                }
            }
        }
    }
    
    void addFactors() {
        boolean containsXpow = false;
        boolean containsSin = false;
        boolean containsCos = false;
        boolean containsConst = false;
        for (String factorString : factorsString) {
            if (factorString.matches(constant)) {
                if (containsConst) {
                    constFactor.setCoef(new BigInteger(factorString).
                            multiply(constFactor.getCoef()));
                } else {
                    factors.add(constFactor = new Const(factorString));
                    containsConst = true;
                }
            } else if (factorString.matches(sin)) {
                String sinExpo = getExpo(factorString,')');
                if (containsSin) {
                    sinFactor.setSinExpo(new BigInteger(sinExpo).
                            add(sinFactor.getSinExpo()));
                } else {
                    factors.add(sinFactor = new Sin(sinExpo));
                    containsSin = true;
                }
            } else if (factorString.matches(cos)) {
                String cosExpo = getExpo(factorString,')');
                if (containsCos) {
                    cosFactor.setCosExpo(new BigInteger(cosExpo).
                            add(cosFactor.getCosExpo()));
                } else {
                    factors.add(cosFactor = new Cos(cosExpo));
                    containsCos = true;
                }
            } else if (factorString.matches(xpow)) {
                String xpowExpo = getExpo(factorString,'x');
                if (containsXpow) {
                    xpowFactor.setXpowExpo(new BigInteger(xpowExpo).
                            add(xpowFactor.getXpowExpo()));
                } else {
                    factors.add(xpowFactor = new Xpow(xpowExpo));
                    containsXpow = true;
                }
            } else if (factorString.matches(poly)) {
                factors.add(new Poly(factorString.
                        substring(1,factorString.length() - 1)));
            } else if (factorString.matches(composite)) {
                factors.add(new Composite(getCompositeOut(factorString),
                        getExpo(factorString,')'),
                        getCompositeIn(factorString)));
            } else {
                Wrong.WrongFormat();
            }
        }
        if (!coefPositive && containsConst) {
            constFactor.setCoef(constFactor.getCoef().negate());
        } else if (!coefPositive && !containsConst) {
            constFactor = new Const("-1");
            factors.add(constFactor);
        }
    }
    
    ArrayList<Term> derivate() {
        ArrayList<Term> derivation = new ArrayList<>();
        for (Factor factor1 : factors) {
            ArrayList<Factor> temp = new ArrayList<>();
            temp.addAll(factor1.derivate());
            for (Factor factor2 : factors) {
                if (factor1 != factor2) {
                    temp.add(factor2);
                }
            }
            derivation.add(new Term(temp));
        }
        return derivation;
    }
    
    StringBuffer print() {
        StringBuffer output = new StringBuffer();
        //boolean printMultiplicationSign = false;
        boolean hasZeroFactor = false;
        for (int i = 0;i < factors.size();i++) {
            StringBuffer temp = factors.get(i).print();
            /*
            if (printMultiplicationSign) {
                output.append("*");
            }
            output.append(temp);
            printMultiplicationSign = !temp.toString().equals("");*/
            if (temp.toString().equals("0") || temp.toString().equals("(0)") ||
                    temp.toString().equals("((0))") ||
                    temp.toString().equals("(((0)))") ||
                    temp.toString().equals("((((0))))")) {
                hasZeroFactor = true;
            }
            if (i != 0) {
                output.append("*");
            }
            output.append(temp);
        }
        if (hasZeroFactor) {
            output = new StringBuffer("0");
        }
        return output;
    }
    
    static String getExpo(String str,char flag) {
        int len = str.length();
        for (int i = len - 1;i >= 0;i--) {
            if (str.charAt(i) == '^') {
                return str.substring(i + 1,len);
            } else if (str.charAt(i) == flag) {
                return "1";
            }
        }
        return "WRONG FORMAT!";
    }
    
    static String getCompositeIn(String str) {
        int len = str.length();
        int start = 0;
        int end = str.length();
        for (int i = 0;i < str.length();i++) {
            if (str.charAt(i) == '(') {
                start = i + 1;
                break;
            }
        }
        for (int i = len - 1;i >= 0;i--) {
            if (str.charAt(i) == ')') {
                end = i;
                break;
            }
        }
        return str.substring(start,end);
    }
    
    static int getCompositeOut(String str) {
        String type = str.substring(0,3);
        if (type.equals("sin")) {
            return 1; //1:sin
        } else if (type.equals("cos")) {
            return 2; //2:cos
        } else {
            return 3; //3:error
        }
    }
}
/*
class TermTest {
    public static void main(String []args) {
        String testString = "-+3*x*(sin(x)+x^2)*cos(x)^-2*-5*sin((2*x))*x^-2";
        Term testTerm = new Term(testString,true);
        testTerm.splitFactors();
        testTerm.addFactors();
        for (Factor factor : testTerm.factors) {
            System.out.println(factor);
        }
    }
}*/

import java.util.ArrayList;

public class Poly implements Factor {
    private String poly;
    private ArrayList<Term> terms = new ArrayList<>();
    private ArrayList<String> termsString = new ArrayList<>();
    private ArrayList<Boolean> termPositive = new ArrayList<>();
    //String termBegin = "([+-]?\\d+)|(x)|(sin)|(xos)";
    //Pattern termBeginPattern = Pattern.compile(termBegin);
    
    Poly(String poly) {
        this.poly = poly;
        splitTerms();
        addTerms();
    }
    
    void setPoly(String poly) {
        this.poly = poly;
    }
    
    String getPoly() {
        return poly;
    }
    
    Poly(ArrayList<Term> terms) {
        this.terms = terms;
    }
    
    public void splitTerms() {
        String polyTemp = poly + "+";
        if (poly.charAt(0) != '+' && poly.charAt(0) != '-') {
            polyTemp = "+" + polyTemp;
        }
        int bracketCount = 0;
        int i;
        int start;
        for (i = 0;i < polyTemp.length();i++) {
            if (polyTemp.charAt(i) == '+') {
                termPositive.add(true);
            } else if (polyTemp.charAt(i) == '-') {
                termPositive.add(false);
            }
            start = ++i;
            for (; i < polyTemp.length(); i++) {
                if (polyTemp.charAt(i) == '(') {
                    bracketCount++;
                } else if (polyTemp.charAt(i) == ')') {
                    bracketCount--;
                } else if (polyTemp.charAt(i) == '+' ||
                        polyTemp.charAt(i) == '-') {
                    if ((bracketCount == 0 &&
                            polyTemp.charAt(i - 1) != '+' &&
                            polyTemp.charAt(i - 1) != '-' &&
                            polyTemp.charAt(i - 1) != '*' &&
                            polyTemp.charAt(i - 1) != '^')
                            || i == polyTemp.length() - 1) {
                        termsString.add(polyTemp.substring(start, i));
                        i--;
                        break;
                    }
                }
            }
        }
    }
    
    public void addTerms() {
        for (int i = 0;i < termsString.size();i++) {
            terms.add(new Term(termsString.get(i),
                    termPositive.get(i)));
        }
    }
    
    public ArrayList<Factor> derivate() {
        ArrayList<Factor> derivation = new ArrayList<>();
        ArrayList<Term> temp = new ArrayList<>();
        for (Term term : terms) {
            temp.addAll(term.derivate());
        }
        derivation.add(new Poly(temp));
        return derivation;
    }
    
    public StringBuffer print() {
        StringBuffer output = new StringBuffer();
        output.append("(");
        //boolean printPlusSign = false;
        for (int i = 0;i < terms.size();i++) {
            StringBuffer temp = terms.get(i).print();
            /*
            if (printPlusSign) {
                output.append("+");
            }
            output.append(temp);
            printPlusSign = !temp.toString().equals("");*/
            if (i != 0) {
                output.append("+");
            }
            output.append(temp);
        }
        output.append(")");
        return output;
    }
}
/*
class PolyTest {
    public static void main(String []args) {
        String testString =
                "(-1+x^+233)*sin(x^2)*6-+cos(sin(x))*+3*sin(x)+++3+";
        Poly test = new Poly(testString);
        test.splitTerms();
        for (int i = 0;i < test.termsString.size();i++) {
            System.out.println(test.termPositive.get(i) + " : "
                    + test.termsString.get(i));
        }
    }
}*/
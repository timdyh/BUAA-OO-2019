package main;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.TreeSet;

public class Term implements Comparable<Term> {
    private BigInteger one;
    private BigInteger xmi;
    private String ex;
    private TreeSet<Term> ori = new TreeSet<Term>();
    private TreeSet<Term> gra = new TreeSet<Term>();
    private String facts = "sin\\s*\\(.*?\\)(\\s*\\^\\s*[+-]?[0-9]+)?";
    // sin(x)^c
    private String factc = "cos\\s*\\(.*?\\)(\\s*\\^\\s*[+-]?[0-9]+)?";
    // cos(x)^c
    private String factk = "\\(.*\\)";
    // (?)
    private String facx = "x(\\s*\\^\\s*[+-]?[0-9]+)?";// x^c
    private String facsh = "[+-]?[0-9]+";// one

    Term() {
        one = BigInteger.ONE;
        xmi = BigInteger.ZERO;
        ori.clear();
        gra.clear();
        ex = "";
    }

    Term(BigInteger on, BigInteger xm, String ss) {
        one = on;
        xmi = xm;
        ex = ss;
        ori.clear();
        gra.clear();
    }

    public BigInteger getone() {
        return one;
    }

    public BigInteger getxmi() {
        return xmi;
    }

    public String getst() {
        return ex;
    }

    public Term copy() {
        Term ret = new Term(one, xmi, ex);
        return ret;
    }

    private static String kind(String rhs) {
        String facx = "x(\\s*\\^\\s*[+-]?[0-9]+)?";// x^c
        String facts = "sin\\s*\\(.*\\)(\\s*\\^\\s*[+-]?[0-9]+)?";
        String factc = "cos\\s*\\(.*\\)(\\s*\\^\\s*[+-]?[0-9]+)?";
        String factk = "\\(.*\\)";
        String facsh = "[0-9]+";// one
        if (rhs.matches(facx)) {
            return "xmi";
        } else if (rhs.matches(facts)) {
            return "sin";
        } else if (rhs.matches(factc)) {
            return "cos";
        } else if (rhs.matches(factk)) {
            return "kuo";
        } else if (rhs.matches(facsh)) {
            return "one";
        } else {
            return "WF!";
        }
    }

    private String clean(String rhs) {
        int j = 0;
        char[] ch = rhs.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == ' ' || ch[i] == '\t') {
                continue;
            }
            ch[j++] = ch[i];
        }
        String ret = String.valueOf(ch);
        ret = ret.substring(0, j);
        return ret;
    }

    private int findmi(String rhs) {
        for (int i = rhs.length() - 1; i >= 0; i--) {
            if (rhs.substring(i).matches("\\^\\s*[+-]?[0-9]+")) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean isfac(String rhs) {
        if (rhs.matches(facsh)) {
            return true;
        }
        if (rhs.matches(facx)) {
            return true;
        }
        if (rhs.matches(facts)) {
            return true;
        }
        if (rhs.matches(factc)) {
            return true;
        }
        if (rhs.matches(factk)) {
            return true;
        }
        return false;
    }

    private String del(String rhs) {
        char[] ch = rhs.toCharArray();
        int j = 0;
        for (int i = 0;i < rhs.length();i++) {
            if (rhs.charAt(i) != ' ' && rhs.charAt(i) != '\t') {
                ch[j++] = rhs.charAt(i);
            }
        }
        return String.valueOf(ch).substring(0,j);
    }
    
    private int help(String st,String or,String sc, int rev) {
        int temp = findmi(st);
        BigInteger mi;
        if (temp == -1) {
            mi = BigInteger.ONE;
        } else {
            mi = new BigInteger(clean(st.substring(temp + 1, st.length())));
        }
        BigInteger on;
        if (mi.abs().compareTo(BigInteger.valueOf(10000)) == 1) {
            return 0;
        }
        if (rev == 1) {
            on = mi.negate();
        } else {
            on = mi;
        }
        if (mi.equals(BigInteger.ZERO)) {
            ori.add(new Term(BigInteger.ONE, BigInteger.ZERO, ""));
        } else {
            if (temp == -1) {
                temp = st.length();
            }
            String nxt = st.substring(3, temp).trim();
            nxt = nxt.substring(1,nxt.length() - 1).trim();
            if (!isfac(nxt)) {
                return 0;
            }
            String qian = or + "(" + del(nxt) + ")^" +
                    mi.subtract(BigInteger.ONE).toString() + "*";
            if (mi.equals(BigInteger.ONE)) {
                qian = "";
            }
            String hou = sc + "(" + del(nxt) + ")";
            String houu;
            if (mi.equals(BigInteger.ONE)) {
                houu = "";
            } else {
                houu = "^" + mi.toString();
            }
            ori.add(new Term(one, xmi, or + "(" + del(nxt) + ")" + houu));
            gra.add(new Term(on, xmi,  qian + hou));
            Function tt = new Function();
            if (tt.init(nxt) == 0) {
                return 0;
            }
            gra = tt.mergemul(gra, tt.getgra());
        }
        return 1;
    }

    public void onerev() {
        one = one.negate();
    }
    
    public int init(String st) {
        String kid = kind(st);
        if (kid == "WF") {
            return 0;
        } else if (kid == "one") {
            ori.add(new Term(new BigInteger(st), BigInteger.ZERO, ""));
        } else if (kid == "xmi") {
            int temp = findmi(st);
            BigInteger mi;
            if (temp == -1) {
                mi = BigInteger.ONE;
            } else {
                mi = new BigInteger(clean(st.substring(temp + 1, st.length())));
            }
            if (mi.abs().compareTo(BigInteger.valueOf(10000)) == 1) {
                return 0;
            }
            ori.add(new Term(one, mi, ""));
            gra.add(new Term(mi, mi.subtract(BigInteger.ONE), ""));
        } else if (kid == "sin") {
            if (help(st, "sin","cos", 0) == 0) {
                return 0;
            }
        } else if (kid == "cos") {
            if (help(st, "cos","sin", 1) == 0) {
                return 0;
            }
        } else {
            Function tt = new Function();
            if (tt.init(st.substring(1, st.length() - 1)) == 0) {
                return 0;
            }
            ori = tt.getori();
            gra = tt.getgra();
        }
        return 1;
    }

    private static String merge(String aa, String bb) {
        if (aa.equals("") || aa.equals("1")) {
            return bb;
        }
        if (bb.equals("") || bb.equals("1")) {
            return aa;
        }
        return "(" + aa + ")" + "*" + "(" + bb + ")";
    }

    public static Term merge(Term aa, Term bb) {
        Term ret = new Term(aa.getone().multiply(bb.getone()),
            aa.getxmi().add(bb.getxmi()),
                merge(aa.getst(), bb.getst()));
        return ret;
    }

    public TreeSet<Term> getori() {
        return ori;
    }

    public TreeSet<Term> getgra() {
        return gra;
    }

    public void rev() {
        TreeSet<Term> ret = new TreeSet<Term>();
        ret.clear();
        for (Iterator<Term> it = ori.iterator(); it.hasNext();) {
            Term tp = it.next();
            ret.add(new Term(tp.getone().negate(), tp.getxmi(), tp.getst()));
        }
        ori = ret;
        ret = new TreeSet<Term>();
        for (Iterator<Term> it = gra.iterator(); it.hasNext();) {
            Term tp = it.next();
            ret.add(new Term(tp.getone().negate(), tp.getxmi(), tp.getst()));
        }
        gra = ret;
    }

    @Override
    public String toString() {
        return one.toString() + " " + xmi.toString() + " " + ex;
    }

    @Override
    public int compareTo(Term o) {
        if (this.one.compareTo(o.one) == -1) {
            return -1;
        } else if (this.one.compareTo(o.one) == 1) {
            return 1;
        } else if (this.xmi.compareTo(o.xmi) == -1) {
            return -1;
        } else if (this.xmi.compareTo(o.xmi) == 1) {
            return 1;
        } else {
            if (ex.compareTo(o.ex) == 0) {
                return -1;
            }
            return ex.compareTo(o.ex);
        }
    }
}

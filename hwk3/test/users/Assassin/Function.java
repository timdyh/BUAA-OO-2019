package main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    private TreeSet<Term> ori = new TreeSet<Term>();
    private TreeSet<Term> gra = new TreeSet<Term>();
    private String funstr;
    private String cl;
    private TreeSet<String> valchars = new TreeSet<String>();

    Function() {
        ori.clear();
        gra.clear();
        funstr = "";
        String val = "     cosinx()^*0123456789+-";
        for (int i = 0; i < val.length(); i++) {
            valchars.add(String.valueOf(val.charAt(i)));
        }
    }

    private int findwei(int x, char ch, int rev) {
        for (int i = x + 1; i < funstr.length(); i++) {
            if (rev == 1) {
                if (funstr.charAt(i) != ch) {
                    return i;
                }
            } else {
                if (funstr.charAt(i) == ch) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String scan(String rhs, char nxt) {
        int bo = 0;
        if (nxt <= '9' && nxt >= '0') {
            bo = 1;
        }
        if (rhs.matches("\\s*[+-]\\s*[+-]\\s*[+-]")) {
            if (bo == 1) {
                return "+++";
            }
            return "WF!";
        } else if (rhs.matches("\\s*[+-]\\s*[+-]\\s*")) {
            return "++";
        } else if (rhs.matches("\\s*[+-]\\s*")) {
            return "+";
        } else if (rhs.matches("\\s*\\*\\s*[+-]")) {
            if (bo == 1) {
                return "*+";
            }
            return "WF!";
        } else if (rhs.matches("\\s*\\*\\s*")) {
            return "*";
        }
        return "WF!";
    }

    private int sumof(String rhs) {
        int ret = 0;
        for (int i = 0; i < rhs.length(); i++) {
            if (rhs.charAt(i) == '-') {
                ret ^= 1;
            }
        }
        return ret;
    }

    public TreeSet<Term> mergeadd(TreeSet<Term> aa, TreeSet<Term> bb) {
        TreeSet<Term> ret = new TreeSet<Term>();
        for (Iterator<Term> it = bb.iterator(); it.hasNext();) {
            ret.add(it.next());
        }
        for (Iterator<Term> it = aa.iterator(); it.hasNext();) {
            ret.add(it.next());
        }
        return ret;
    }

    public TreeSet<Term> mergemul(TreeSet<Term> aa, TreeSet<Term> bb) {
        TreeSet<Term> ret = new TreeSet<Term>();
        for (Iterator<Term> it = aa.iterator(); it.hasNext();) {
            Term aaa = it.next();
            for (Iterator<Term> it2 = bb.iterator(); it2.hasNext();) {
                Term bbb = it2.next();
                ret.add(Term.merge(aaa, bbb));
            }
        }
        return ret;
    }

    private String chuli(String rhs)
    {
        char[] ch = rhs.toCharArray();
        int sum = 0;
        for (int i = 0;i < rhs.length();i++) {
            if (rhs.charAt(i) == ')') {
                sum--;
            }
            if (sum > 0) {
                ch[i] = '$';
            }
            if (sum < 0) {
                return "";
            }
            if (rhs.charAt(i) == '(') {
                sum++;
            }
        }
        return String.valueOf(ch);
    }
    
    private int build() {
        int now = 0;
        int f = 0;
        int bo = 0;
        TreeSet<Term> calcori = new TreeSet<Term>();
        calcori.add(new Term());
        TreeSet<Term> calcgra = new TreeSet<Term>();
        while (true) {
            int nxt = findwei(now, '#', 0);
            if (nxt == -1) {
                if (now != cl.length()) {
                    return 0;
                }
                break;
            }
            String kid = scan(cl.substring(now, nxt), cl.charAt(nxt));
            f = sumof(cl.substring(now, nxt));
            if (kid == "WF!") {
                return 0;
            }
            if (kid == "+++" || kid == "++" || kid == "+") {
                if (bo == 1) {
                    ori = mergeadd(ori, calcori);
                    gra = mergeadd(gra, calcgra);
                } else {
                    bo = 1;
                }
                calcori.clear();
                calcori.add(new Term());
                calcgra.clear();
            }
            now = nxt;
            nxt = findwei(now, '$', 1);
            if (nxt == -1) {
                nxt = funstr.length();
            }
            Term term = new Term();
            if (term.init(cl.substring(now, nxt)) == 0) {
                return 0;
            }
            if (f == 1) {
                term.rev();
            }
            calcgra = mergeadd(mergemul(calcori, term.getgra()),
                mergemul(calcgra, term.getori()));
            calcori = mergemul(calcori, term.getori());
            now = nxt;
        }
        ori = mergeadd(ori, calcori);
        gra = mergeadd(gra, calcgra);
        return 1;
    }

    private void found(String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(funstr);
        while (m.find()) {
            int l = m.start();
            int r = m.end();
            char[] ch = funstr.toCharArray();
            ch[l] = '#';
            for (int i = l + 1; i < r; i++) {
                ch[i] = '$';
            }
            funstr = String.valueOf(ch);
        }
    }
    
    private static TreeSet<Term> optimize(TreeSet<Term> rhs) {
        ArrayList<Term> tpa = new ArrayList<Term>();
        for (Iterator<Term> it = rhs.iterator(); it.hasNext();) {
            Term temp = it.next();
            int bo = 0;
            for (int i = 0; i < tpa.size(); i++) {
                Term now = tpa.get(i).copy();
                if (now.getxmi().equals(temp.getxmi()) 
                    && now.getst().equals(temp.getst())) {
                    bo = 1;
                    now = new Term(temp.getone().add(now.getone()),
                        now.getxmi(), now.getst());
                    tpa.remove(i);
                    tpa.add(now);
                    break;
                }
            }
            if (bo == 0) {
                tpa.add(temp.copy());
            }
        }
        TreeSet<Term> ret = new TreeSet<Term>();
        for (int i = 0;i < tpa.size();i++) {
            ret.add(tpa.get(i));
        }
        return ret;
    }

    public TreeSet<Term> getori() {
        return optimize(ori);
    }

    public TreeSet<Term> getgra() {
        return optimize(gra);
    }

    public int init(String rhs) {
        funstr = rhs;
        for (int i = 0; i < funstr.length(); i++) {
            if (!valchars.contains(String.valueOf(funstr.charAt(i)))) {
                return 0;
            }
        }
        funstr = funstr.trim();
        if (funstr.length() == 0) {
            return 0;
        }
        if (funstr.charAt(0) != '+' && funstr.charAt(0) != '-') {
            funstr = '+' + funstr;
        }
        cl = funstr;
        funstr = chuli(funstr);
        if (funstr.length() == 0) {
            return 0;
        }
        String facts = "sin\\s*\\(.*?\\)(\\s*\\^\\s*[+-]?[0-9]+)?";
        // sin(x)^c
        found(facts);
        String factc = "cos\\s*\\(.*?\\)(\\s*\\^\\s*[+-]?[0-9]+)?";
        // cos(x)^c
        found(factc);
        String factk = "\\(.*?\\)";
        // (?)
        found(factk);
        String facx = "x(\\s*\\^\\s*[+-]?[0-9]+)?";// x^c
        found(facx);
        String facsh = "[0-9]+";// one
        found(facsh);
        int ret = build();
        return ret;
    }

    public String output(TreeSet<Term> tt) {
        String ret = "";
        for (Iterator<Term> it = tt.iterator(); it.hasNext();) {
            Term ttt = it.next();
            Term temp = ttt.copy();
            int bo = 0;
            if (temp.getone().equals(BigInteger.ZERO)) {
                continue;
            }
            if (temp.getone().signum() == 1) {
                ret = ret + "+";
            }
            if (temp.getone().signum() == -1) {
                ret = ret + "-";
                temp.onerev();
            }
            if (temp.getone().compareTo(BigInteger.ONE) != 0) {
                ret = ret + temp.getone().toString();
                bo = 1;
            }
            if (!temp.getxmi().equals(BigInteger.ZERO)) {
                if (bo == 1) {
                    ret = ret + "*";
                }
                ret = ret + "x";
                bo = 1;
                if (!temp.getxmi().equals(BigInteger.ONE)) {
                    ret = ret + "^" + temp.getxmi().toString();
                }
            }
            if (!temp.getst().equals("")) {
                if (bo == 1) {
                    ret = ret + "*";
                }
                ret = ret + temp.getst();
                bo = 1;
            }
            if (bo == 0) {
                ret = ret + "1";
            }
        }
        if (ret == "") {
            ret = "0";
        }
        return ret;
    }
}

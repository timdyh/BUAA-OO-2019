import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Expr extends Factor {
    private StringBuilder sb;
    private ArrayList<Term> terms = new ArrayList<>();

    public Expr(String str) {
        super(str);
        sb = new StringBuilder(str);
        pretreat();
        // System.out.println("@Expr "+ sb);
        Pattern patternExpr =
                Pattern.compile("[-+][-+]?(#.+?)+((?=[-+]{1,2}#)|$)");
        Matcher matcherExpr = patternExpr.matcher(sb);
        int preEndIndex = 0;
        while (matcherExpr.find()) {
            if (matcherExpr.start() != preEndIndex) {
                // System.out.println("Expr: 匹配起点非上一次终点");
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            preEndIndex = matcherExpr.end();

            Term term = new Term(matcherExpr.group());
            terms.add(term);
        }
        if (preEndIndex < sb.length()) {
            // System.out.println("Expr: 剩余未匹配");
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }

    private void pretreat() {
        // 去皮
        sb = new StringBuilder(sb.substring(1, sb.length() - 1));

        // 插入辅助+
        if (sb.charAt(0) != '-' && sb.charAt(0) != '+') {
            sb = sb.insert(0, '+');
        }
        // 插入辅助*
        Pattern patternOp = Pattern.compile("(?<![-+*^])[-+]{1,2}");
        Matcher matcherOp = patternOp.matcher(sb);
        int cnt;
        int nextStart = 0;
        while (matcherOp.find(nextStart)) {
            nextStart = matcherOp.end();
            cnt = 0;
            for (int i = 0; i < nextStart; i++) {
                if (sb.charAt(i) == '(') {
                    cnt++;
                }
                if (sb.charAt(i) == ')') {
                    cnt--;
                }
            }
            if (cnt == 0) {
                sb.insert(nextStart, '*');
            }
            matcherOp = patternOp.matcher(sb);
        }

        // 标记最外层的*
        cnt = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '(') {
                cnt++;
            }
            if (sb.charAt(i) == ')') {
                cnt--;
            }
            if (sb.charAt(i) == '*' && cnt == 0) {
                sb = sb.replace(i, i + 1, "#");
            }
        }
    }

    public String diff() {
        String result = "";
        if (terms.isEmpty()) {
            return "0";
        }

        for (int i = 0; i < terms.size(); i++) {
            result += "+" + terms.get(i).diff();
        }
        result = result.substring(1);

        result = "(" + result + ")";

        return result;
    }
}
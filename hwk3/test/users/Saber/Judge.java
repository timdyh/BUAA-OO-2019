import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Judge {
    private String base1 = "[-\\+]{0,2}(((x|(sin\\(x\\))|(cos\\(x\\)))" +
            "(\\^[-\\+]?\\d+)?)|[-\\+]?\\d+)(\\*(((x|(sin\\(x\\))|" +
            "(cos\\(x\\)))(\\^[-\\+]?\\d+)?)|([-\\+]?\\d+)))*";
    private String base2 = "([-\\+]{1,2}(((x|(sin\\(x\\))|" +
            "(cos\\(x\\)))(\\^[-\\+]?\\d+)?)|[-\\+]?\\d+)(\\*(((x|" +
            "(sin\\(x\\))|(cos\\(x\\)))(\\^[-\\+]?\\d+)?)|([-\\+]?\\d+)))*)*";
    private String yinzi = "(((x|(sin\\(x\\))|(cos\\(x\\)))" +
            "(\\^[-\\+]?\\d+)?)|([-\\+]?\\d+))";
    private String base = "(sin\\(" + yinzi + "\\))" + "|" +
            "(cos\\(" + yinzi + "\\))" + "|" +
            "(\\(" + base1 + base2 + "\\))";

    public boolean firstJudge(String line) {
        Pattern p1 = Pattern.compile("([0-9]+( )+[0-9]+)|" +
                "(\\^( )*[-\\+]( )+[0-9]+)|([-\\+]( )*[-\\+]( )*" +
                "[-\\+]( )+[0-9]+)|(\\*( )*[-\\+]( )+[0-9]+)|" +
                "((s +in)|(s +i +n)|(si +n)|(c +os)|(c +o +s)|(co +s))");
        Matcher m1 = p1.matcher(line);
        return (m1.find() || line.length() == 0);
    }

    public boolean secondJudge(String line) {
        Pattern q = Pattern.compile("\\^[-\\+]?\\d+");
        Matcher n = q.matcher(line);
        int flag = 0;
        while (n.find()) {
            String a = n.group();
            Pattern one = Pattern.compile("[-\\+]?\\d+");
            Matcher two = one.matcher(a);
            if (two.find()) {
                BigInteger index = new BigInteger(two.group());
                BigInteger max = new BigInteger("10000");
                if (index.abs().compareTo(max) == 1) {
                    flag = 1;
                    break;
                }
            }
        }
        return flag == 1;
    }

    public boolean thirdJudge(String line) {
        int flag = 0;
        Pattern first = Pattern.compile("\\(" + base1 + base2 +
                "\\)\\^[-\\+]?\\d+");
        Matcher second = first.matcher(line);
        while (second.find()) {
            if (second.start() != 0) {
                int start = second.start();
                if (line.charAt(start - 1) != 'n' &&
                        line.charAt(start - 1) != 's') {
                    flag = 1;
                    break;
                }
            } else {
                flag = 1;
                break;
            }
        }
        return flag == 1;
    }

    public boolean fourthJudge(String line) {
        String line1 = line;
        int flag = 0;
        while (true) {
            if (this.thirdJudge(line1)) {
                flag = 1;
                break;
            }
            line1 = line1.replaceAll(base, "x");
            Pattern p = Pattern.compile(base);
            Matcher m = p.matcher(line1);
            if (!m.find()) {
                break;
            }
        }
        Pattern p = Pattern.compile("([-\\+]{1,2}((x(\\^[-\\+]?\\d+)?)" +
                "|[-\\+]?\\d+)" +
                "(\\*((x(\\^[-\\+]?\\d+)?)|([-\\+]?\\d+)))*)+");
        Matcher m = p.matcher(line1);
        return !m.matches() || flag == 1;
    }
}

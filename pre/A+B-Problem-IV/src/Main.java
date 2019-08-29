import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        string = "+" + string;

        Pattern pattern = Pattern.compile("\\s*([+-])\\s*([+-]?)(\\d+)");
        Matcher matcher = pattern.matcher(string);

        BigInteger ans = new BigInteger("0");
        while (matcher.find()) {
            BigInteger tmp =
                    new BigInteger(matcher.group(2) + matcher.group(3));
            if (matcher.group(1).equals("+")) {
                ans = ans.add(tmp);
            } else {
                ans = ans.subtract(tmp);
            }
        }
        System.out.println(ans);
    }
}
import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();

        Pattern pattern =
                Pattern.compile("\\s*([+-]?)(\\d+)\\s+([+-]?)(\\d+)\\s*");
        Matcher matcher = pattern.matcher(string);

        if (matcher.matches()) {
            BigInteger a =
                    new BigInteger(matcher.group(1) + matcher.group(2));
            BigInteger b =
                    new BigInteger(matcher.group(3) + matcher.group(4));
            System.out.println(a.add(b));
        } else {
            System.out.println("WRONG FORMAT!");
        }
    }
}
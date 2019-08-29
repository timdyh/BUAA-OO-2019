import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String []args) {
        try {
            Scanner scan = new Scanner(System.in);
            if (!scan.hasNextLine()) {
                Wrong.WrongFormat();
            }
            String input = scan.nextLine();
            input = spaceCheck(input);
            Poly expression = new Poly(input);
            Poly afterDerivate = (Poly) expression.derivate().get(0);
            StringBuffer afterDerivateStringBuffer = afterDerivate.print();
            /*
            String afterDerivateString = afterDerivateStringBuffer.substring(
                    1,afterDerivateStringBuffer.length() - 1);
            Poly newInput = new Poly(afterDerivateString);
            StringBuffer output = newInput.print();
            */
            System.out.println(afterDerivateStringBuffer.
                    substring(1,afterDerivateStringBuffer.length() - 1));
        } catch (Exception ex) {
            Wrong.WrongFormat();
        }
    }
    
    private static String spaceCheck(String temp) {
        String illegalCharError = "([^ \\t)(0-9sincox*^+-])";
        String spaceError1 = "([+-]\\s*[+-]\\s*[+-]\\s+\\d+)"; //+++ 13
        String spaceError2 = "([*^]\\s*[+-]\\s+\\d+)"; //*+ 3  ^- 6
        String spaceError3 = "(\\d+\\s+\\d+)"; //123 456
        String spaceError4 = "(s\\s+i\\s*n)|(s\\s*i\\s+n)|" +
                "(c\\s+o\\s*s)|(c\\s*o\\s+s)"; //si n
        String spaceError5 = "(sin|cos)\\s*\\(\\s*[+-]\\s+\\d+\\s*\\)";
        //sin(- 10)
        String spaceError = spaceError1 + "|" + spaceError2 + "|"
                + spaceError3 + "|" + spaceError4 + "|" + spaceError5;
        Pattern illegalChar = Pattern.compile(illegalCharError);
        Pattern space = Pattern.compile(spaceError);
        Matcher illegalCharMatcher = illegalChar.matcher(temp);
        Matcher spaceMatcher = space.matcher(temp);
        if (illegalCharMatcher.find() || spaceMatcher.find()) {
            Wrong.WrongFormat();
        }
        return temp.replaceAll("\\s","");
    }
}

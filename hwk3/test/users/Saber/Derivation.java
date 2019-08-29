import java.util.Scanner;

public class Derivation {

    public static String change(String line) {
        String line1 = line;
        line1 = line1.replaceAll("-\\+\\+|\\+\\+-|\\+-\\+|---", "-");
        line1 = line1.replaceAll("\\+\\+\\+|\\+--|--\\+|-\\+-", "\\+");
        line1 = line1.replaceAll("\\+-|-\\+", "-");
        line1 = line1.replaceAll("--|\\+\\+", "\\+");
        line1 = line1.replaceAll("\\*\\+", "\\*");
        line1 = line1.replaceAll("\\(\\+", "\\(");
        return line1;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        if (input.hasNextLine()) {
            String line = input.nextLine();
            line = line.replaceAll("\t", " ");
            Judge j = new Judge();
            if (j.firstJudge(line)) {
                System.out.println("WRONG FORMAT!");
            } else {
                line = line.replaceAll(" ", "");
                if (line.length() == 0) {
                    System.out.println("WRONG FORMAT!");
                } else {
                    if (line.charAt(0) != '+' && line.charAt(0) != '-') {
                        line = "+" + line;
                    }
                    if (j.secondJudge(line) || j.fourthJudge(line)) {
                        System.out.println("WRONG FORMAT!");
                    } else {
                        line = change(line);
                        Found a = new Found(line);
                        String result = a.getResult();
                        result = change(result);
                        if (result.length() == 0) {
                            result = "0";
                        } else if (result.charAt(0) == '+') {
                            result = result.substring(1, result.length());
                        }
                        System.out.println(result);
                    }
                }
            }
        } else {
            System.out.println("WRONG FORMAT!");
        }
    }
}

import java.util.Scanner;

public class PolyCal {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        if (scan.hasNextLine()) {
            String str = scan.nextLine();

            InputHandler in = new InputHandler(str);
            str = in.getCleanPoly();

            Poly poly = new Poly(str);

            Factor res = poly.diff().get(0);

            OutputHandler out = new OutputHandler(res.print());
            System.out.println(out.print());
            System.exit(0);
        } else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }
}
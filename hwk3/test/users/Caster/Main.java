package derivation;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        if (scan.hasNextLine()) {
            String string = scan.nextLine();
            try {
                PolyTree polyTree = new PolyTree(string);
                polyTree.BuildPolyTree();
                ArrayList<String> terms = polyTree.Derivation();
                String result = polyTree.PrintTree(terms);
                PolyTree resultPoly = new PolyTree(result);
                String print = resultPoly.GetInput();
                print = resultPoly.Simplify(print);
                System.out.print(print);
            } catch (Exception e) {
                System.out.print("WRONG FORMAT!");
            }
        } else {
            System.out.print("WRONG FORMAT!");
        }
    }
}
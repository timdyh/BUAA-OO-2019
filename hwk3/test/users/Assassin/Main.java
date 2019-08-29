package main;

import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        if (!cin.hasNextLine()) {
            System.out.println("WRONG FORMAT!");
        } else {
            String st = cin.nextLine();
            Function temp = new Function();
            if (temp.init(st) == 0) {
                System.out.println("WRONG FORMAT!");
            } else {
                String ans = temp.output(temp.getgra());
                if (ans.charAt(0) == '+') {
                    ans = ans.substring(1);
                }
                System.out.println(ans);
            }
        }
        cin.close();
    }
}

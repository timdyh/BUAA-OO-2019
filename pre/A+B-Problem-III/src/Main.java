import java.util.Scanner;
import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int radix1 = scanner.nextInt();
        BigInteger a = scanner.nextBigInteger(radix1);
        int radix2 = scanner.nextInt();
        BigInteger b = scanner.nextBigInteger(radix2);
        int radix3 = scanner.nextInt();

        BigInteger sum = a.add(b);
        String ans = sum.toString(radix3);
        System.out.println(ans);
    }
}
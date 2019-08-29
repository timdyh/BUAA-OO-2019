package homework;

public class Main {
    public static void main(String[] args) {
        InputHandler polyIn = new InputHandler();
        String poly = polyIn.getStr();
        Analysis polyDeal = new Analysis(poly);
    }
}

import com.oocourse.specs3.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        // long startTime = System.currentTimeMillis();
        AppRunner runner = AppRunner.newInstance(MyPath.class, MyRailwaySystem.class);
        runner.run(args);
        // long endTime = System.currentTimeMillis();
        // System.out.println("Time used: " + (endTime - startTime) + "ms");
    }
}
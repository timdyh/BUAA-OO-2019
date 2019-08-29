import com.oocourse.specs2.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        // long startTime = System.currentTimeMillis();
        AppRunner runner = AppRunner.newInstance(MyPath.class, MyGraph.class);
        runner.run(args);
        // long endTime = System.currentTimeMillis();
        // System.out.println("Time used: " + (endTime - startTime) + "ms");
    }
}
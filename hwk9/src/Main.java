import com.oocourse.specs1.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        // long startTime = System.currentTimeMillis();
        AppRunner runner =
                AppRunner.newInstance(MyPath.class, MyPathContainer.class);
        runner.run(args);
        // long endTime = System.currentTimeMillis();
        // System.out.println("Time used: " + (endTime - startTime) + "ms");
    }
}

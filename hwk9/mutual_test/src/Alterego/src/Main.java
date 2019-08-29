import com.oocourse.specs1.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        AppRunner runner = AppRunner.newInstance(
                Mypath2.class, MyPathContainer2.class);
        runner.run(args);
    }
}

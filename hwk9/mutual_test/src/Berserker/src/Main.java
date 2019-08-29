import com.oocourse.specs1.AppRunner;
import custompath.RealPath;
import custompath.RealPathContainer;

public class Main {

    public static void main(String[] args) throws Exception {
        AppRunner runner = AppRunner.newInstance(
                RealPath.class, RealPathContainer.class);
        runner.run(args);
    }
}

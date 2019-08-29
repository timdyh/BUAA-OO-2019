import com.oocourse.specs1.AppRunner;

import path.MyPath;
import path.MyPathContainer;

public class Main {
    public static void main(String[] args) throws Exception {
        AppRunner runner = AppRunner.newInstance(MyPath.class,
            MyPathContainer.class);

        runner.run(args);
    }
}

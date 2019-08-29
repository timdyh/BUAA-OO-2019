import com.oocourse.specs1.AppRunner;
//import com.oocourse.specs1.MyPath;
//import com.oocourse.specs1.MyPathContainer;

public class Main {
    public static void main(String[] args) throws Exception {
        AppRunner runner = AppRunner.newInstance(MyPath.class,
                MyPathContainer.class);
        runner.run(args);

    }
}

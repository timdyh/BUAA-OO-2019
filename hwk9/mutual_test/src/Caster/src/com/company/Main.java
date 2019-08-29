package com.company;

import com.oocourse.specs1.AppRunner;
import mycode.MyPath;
import mycode.MyPathContainer;

public class Main {
    public static void main(String[] args) {
        AppRunner runner = null;
        try {
            runner = AppRunner.newInstance(MyPath.class, MyPathContainer.class);
        } catch (Exception e) {
            System.err.println("error");
        }
        runner.run(args);
    }
}

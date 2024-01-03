package com.jkfd.oopii;



import com.jkfd.oopii.Controller.MonthViewController;

import com.jkfd.oopii.Controller.PopUpController;
import com.jkfd.oopii.Utils.SQLiteDB;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        MonthViewController.loadView(stage);
        SQLiteDB db = new SQLiteDB();

        db.displayAllData();
    }

    public static void main(String[] args) throws Exception {
        launch();

    }
}
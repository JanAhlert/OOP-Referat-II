package com.jkfd.oopii;



import com.jkfd.oopii.Controller.MonthViewController;

import com.jkfd.oopii.Database.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public static DatabaseManager databaseManager;

    @Override
    public void start(Stage stage) throws Exception {

        MonthViewController.loadView(stage);

        databaseManager.GetEvents();
    }

    public static void main(String[] args) throws Exception {
        databaseManager = new DatabaseManager("sqlite");

        launch();
    }
}
package com.jkfd.oopii;



import com.jkfd.oopii.Controller.MonthViewController;

import com.jkfd.oopii.Database.DatabaseManager;
import com.jkfd.oopii.Database.Models.Event;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class HelloApplication extends Application {
    public static DatabaseManager databaseManager;

    @Override
    public void start(Stage stage) throws Exception {

        MonthViewController.loadView(stage);

        // TODO: ONLY A TEST, SHOULD BE DELETED LATER!
        ArrayList<Event> test = databaseManager.GetEvents();
        for (Event tmp : test) {
            System.out.println(tmp.title);
        }
    }

    public static void main(String[] args) throws Exception {
        databaseManager = new DatabaseManager("sqlite");

        launch();
    }
}
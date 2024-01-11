package com.jkfd.oopii;



import com.jkfd.oopii.Controller.MonthViewController;

import com.jkfd.oopii.Database.DatabaseManager;
import com.jkfd.oopii.Database.Models.Event;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

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
        // Delete the data directory for development purposes
        if (Arrays.stream(args).toList().contains("deletedb")) {
            File db = new File("./data/app.db");
            boolean deleted = db.delete();
            if (!deleted) {
                System.out.println("Debug warning: Database was not deleted even though 'deletedb' was supplied");
            }
        }

        databaseManager = new DatabaseManager("sqlite");

        launch();
    }
}
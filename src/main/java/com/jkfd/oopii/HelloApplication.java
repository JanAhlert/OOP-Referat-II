package com.jkfd.oopii;



import com.jkfd.oopii.Controller.MonthViewController;

import com.jkfd.oopii.Database.DatabaseManager;
import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;
import com.jkfd.oopii.Database.Repository.SQLiteDB;
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
        ArrayList<Todo> testen = databaseManager.GetTodos();
        for (Todo tmp : testen) {
            System.out.println(tmp.getTitle());
        }

    }

    public static void main(String[] args) throws Exception {
        databaseManager = new DatabaseManager("sqlite");

        launch();
    }
}
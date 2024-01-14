package com.jkfd.oopii;



import com.jkfd.oopii.Controller.MonthViewController;

import com.jkfd.oopii.Database.DatabaseManager;
import com.jkfd.oopii.Subsystem.SubsystemManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public class HelloApplication extends Application {
    public static DatabaseManager databaseManager;
  
    static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);
 
    @Override
    public void start(Stage stage) {
        MonthViewController.loadView(stage);
    }

    public static void main(String[] args) throws Exception {
        // Delete the data directory for development purposes
        if (Arrays.stream(args).toList().contains("deletedb")) {
            File db = new File("./data/app.db");
            boolean deleted = db.delete();
            if (!deleted) {
                logger.warn("Debug warning: Database was not deleted even though 'deletedb' was supplied");
            }
        }

        databaseManager = new DatabaseManager("sqlite");
        SubsystemManager subsystemManager = new SubsystemManager();

        launch();

        subsystemManager.Shutdown();
    }
}
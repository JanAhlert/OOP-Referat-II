package com.jkfd.oopii;



import com.jkfd.oopii.Controller.WeekViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

       WeekViewController.loadView(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
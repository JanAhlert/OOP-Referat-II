package com.jkfd.oopii;

import com.jkfd.oopii.Controller.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        HomeController.loadView(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
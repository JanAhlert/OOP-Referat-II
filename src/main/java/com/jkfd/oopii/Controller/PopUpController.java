package com.jkfd.oopii.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class PopUpController {

    private static FXMLLoader fxmlLoader = new FXMLLoader(PopUpController.class.getResource("/com/jkfd/oopii/PopUpEdit.fxml"));    //FXMLLoader for the pop-upView

    /**
     * Method to load the pop-upView file and set the scene in the stage
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException {

            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Termin bearbeiten");

    }
}

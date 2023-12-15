package com.jkfd.oopii.Controller;

import com.jkfd.oopii.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controler for the Home View
 * @Author: Fabian Klein
 * @Version: 0.1
 */
public class HomeController implements Initializable{
    @FXML
    Label CurrentDate;

    Date currentDate = new Date();

    /**
     * Initializes the home-view
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class.getResource("/com/jkfd/oopii/home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HomeController.class.getResource("/com/jkfd/oopii/style.css").toExternalForm());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();

    }

    public void displayCurrentDate()
    {
        CurrentDate.setText(currentDate.getCurrentDate());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }





}




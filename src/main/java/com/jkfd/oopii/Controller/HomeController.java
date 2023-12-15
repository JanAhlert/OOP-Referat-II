package com.jkfd.oopii.Controller;

import com.calendarfx.view.MonthView;
import com.jkfd.oopii.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
    Pane MonthViewPane;

    Date currentDate = new Date();

    /**
     * Initializes the home-view
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class.getResource("/com/jkfd/oopii/month-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();

        MonthView monthView = new MonthView();
        monthView.setMinHeight(800);             //ToDO Höhe anpassen bitte
        monthView.setMinWidth(1446);

        HomeController homeController = fxmlLoader.getController();
        homeController.MonthViewPane.getChildren().add(monthView);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }


}




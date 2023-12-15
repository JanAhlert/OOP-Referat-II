package com.jkfd.oopii.Controller;

import com.calendarfx.view.MonthView;
import com.jkfd.oopii.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the month-view
 */
public class MonthViewController {

    @FXML
    Pane MonthViewPane;

    Date currentDate = new Date();

    /**
     * Initializes the home-view
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(MonthViewController.class.getResource("/com/jkfd/oopii/month-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
        //Erstellung der MonthView aus dem CalendarFX
        MonthView monthView = new MonthView();
        monthView.setMinHeight(795);
        monthView.setMinWidth(1486);
        MonthViewController monthViewController = fxmlLoader.getController();
        monthViewController.MonthViewPane.getChildren().add(monthView);
    }

}




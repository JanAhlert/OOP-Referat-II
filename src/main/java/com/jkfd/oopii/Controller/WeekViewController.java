package com.jkfd.oopii.Controller;

import com.calendarfx.view.page.WeekPage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class WeekViewController {
    @FXML
    Pane WeekViewPane;

    public static void loadView(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeController.class.getResource("/com/jkfd/oopii/week-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();

        WeekPage weekPage = new WeekPage();
        weekPage.setMinHeight(800);             //ToDO Höhe anpassen bitte
        weekPage.setMinWidth(1446);
        weekPage.setShowNavigation(false);
        weekPage.setShowDate(false);
        WeekViewController weekViewController = fxmlLoader.getController();
        weekViewController.WeekViewPane.getChildren().add(weekPage);

    }



}

package com.jkfd.oopii.Controller;

import com.calendarfx.view.YearView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class YearViewController
{

    @FXML
    Pane YearViewPane;

    public static void loadView(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(YearViewController.class.getResource("/com/jkfd/oopii/year-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();

        YearView yearView = new YearView();
        yearView.setMinHeight(795);
        yearView.setMinWidth(1540);

        YearViewController yearViewController = fxmlLoader.getController();
        yearViewController.YearViewPane.getChildren().add(yearView);

    }

}

package com.jkfd.oopii.Controller;

import com.calendarfx.view.MonthView;
import com.calendarfx.view.WeekView;
import com.calendarfx.view.YearView;
import com.calendarfx.view.page.WeekPage;
import com.jkfd.oopii.Date;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.Year;

/**
 * Controller for the month-view
 */
public class MonthViewController {

    @FXML
    Pane MonthViewPane;
    @FXML
    Tab MonthViewTab;
    @FXML
    Tab WeekViewTab;
    @FXML
    Tab YearViewTab;


    /**
     * Initializes the month-view
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

    @FXML
    private void onSelectionMonthViewTab(Event event){
        if (MonthViewTab.isSelected()) {
            MonthView monthView = new MonthView();
            monthView.setMinHeight(795);
            monthView.setMinWidth(1486);
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(monthView);
        }
    }

    @FXML
    private void onSelectionYearViewTab(Event event){
        if (YearViewTab.isSelected()) {
            YearView yearView = new YearView();
            yearView.setMinHeight(795);
            yearView.setMinWidth(1486);
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(yearView);
        }
    }

    @FXML
    private void onSelectionWeekViewTab(Event event){
        if (WeekViewTab.isSelected()) {
            WeekPage weekPage = new WeekPage();
            weekPage.setMinHeight(795);
            weekPage.setMinWidth(1486);
            weekPage.setShowNavigation(false);
            weekPage.setShowDate(false);
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(weekPage);
        }
    }
}



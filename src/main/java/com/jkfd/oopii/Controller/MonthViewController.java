package com.jkfd.oopii.Controller;

import com.calendarfx.view.MonthView;
import com.calendarfx.view.YearView;
import com.calendarfx.view.page.WeekPage;
import javafx.event.Event;
import com.jkfd.oopii.Abstract.AbstractController;
import com.jkfd.oopii.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for the month-view
 */
public class MonthViewController extends AbstractController implements Initializable {

    @FXML
    Pane MonthViewPane;
    @FXML
    Tab MonthViewTab;
    @FXML
    Tab WeekViewTab;
    @FXML
    Tab YearViewTab;
    @FXML
    Label CurrentDateLabel;
    @FXML
    Button NextMonth;
    @FXML
    Button PreviousMonth;


    private static boolean isInitialized = false; //Variable to check if the View is initialized
    Date currentDate = new Date(); //Variable for the Date
    private static MonthView monthView; //Variable for the MonthView um die View zu ändern
    private static YearView yearView = new YearView(); //Variable for the YearView um die View zu ändern
    private static WeekPage weekPage = new WeekPage(); //Variable for the WeekView um die View zu ändern


    static FXMLLoader fxmlLoader = new FXMLLoader(MonthViewController.class.getResource("/com/jkfd/oopii/month-view.fxml"));    //FXMLLoader for the month-view

    /**
     * Method to load the month-view file and set the scene in the stage
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException{
        try {
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim Laden der Home-View.");
            alert.showAndWait();
        }
    }

    /**
     * Method that is loaded when the view is initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Loads the MonthView
        monthView = new MonthView();
        monthView.setMinHeight(795);
        monthView.setMinWidth(1486);
        MonthViewController monthViewController = fxmlLoader.getController();
        monthViewController.MonthViewPane.getChildren().add(monthView);
        isInitialized = true;

        //Loads the YearView
        yearView = new YearView();
        yearView.setMinHeight(795);
        yearView.setMinWidth(1486);

        //Loads the WeekView
        weekPage = new WeekPage();
        weekPage.setMinHeight(795);
        weekPage.setMinWidth(1486);
        weekPage.setShowNavigation(false);
        weekPage.setShowDate(false);

        //Sets the current date in the month-view
        setCurrentDateLabel(Date.MMYYYY);
    }

//---------------------------------------------------Functions for Changing the View by selcting the Tab---------------------------------------------------//

    /**
     * Sets the current date in the month-view
     */
    @FXML
    private void onSelectionMonthViewTab(Event event) {
            if (isInitialized == true) {
                try {
                    MonthViewPane.getChildren().clear();
                    MonthViewPane.getChildren().add(monthView);
                    currentDate.resetSelectedDate();
                    setCurrentDateLabel(Date.MMYYYY);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Ein Fehler ist aufgetreten");
                    alert.setContentText("Es gab ein Problem beim Laden der Monatsansicht.");
                    alert.showAndWait();
                }

            }
    }

    /**
     * Sets the current date in the year-view
     */
    @FXML
    private void onSelectionYearViewTab(Event event){
        try {
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(yearView);
            currentDate.resetSelectedDate();
            setCurrentDateLabel(Date.YYYY);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim Laden der Jahresansicht.");
            alert.showAndWait();
        }
    }

    /**
     * Sets the current date in the week-view
     */
    @FXML
    private void onSelectionWeekViewTab(Event event){
        try {
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(weekPage);
            currentDate.resetSelectedDate();
            setCurrentDateLabel(Date.DDMMYYYY);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim Laden der Wochenansicht.");
            alert.showAndWait();
        }
    }

    /**
     * Setter Methode for the current date in the home-views
     */
    @FXML
    private void setCurrentDateLabel(DateTimeFormatter Format)
    {
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Format));
    }

    //---------------------------------------------------Functions for Changing the Date over the Buttons---------------------------------------------------//

    /**
     *Mehtode for the NextMonth Button //TODO: Rename the Function
     */
    @FXML
    private void setNextMonth()
    {
        if (MonthViewTab.isSelected()) {
            changeMonth(1);
        } else if (WeekViewTab.isSelected()) {
            changeWeek(1);
        } else if (YearViewTab.isSelected()) {
            changeYear(1);
        }
    }

    /**
     *Mehtode for the PreviousMonth Button //TODO: Rename the Function
     */
    @FXML
    private void setPreviousMonth(){
        if (MonthViewTab.isSelected()) {
            changeMonth(-1);
        } else if (WeekViewTab.isSelected()) {
            changeWeek(-1);
        } else if (YearViewTab.isSelected()) {
            changeYear(-1);
        }
    }

    //---------------------------------------------------Functions for Changing the Date---------------------------------------------------//

    /**
     * Changes the month view by the given value
     * @param month
     */
    private void changeMonth(int month) {
        currentDate.setPlusMonth(month);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.MMYYYY));
        monthView.setDate(currentDate.getSelectedDate());
    }

    /**
     * Changes the week view by the given value
     * @param week
     */
    private void changeWeek(int week) {
        currentDate.setPlusWeek(week);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.DDMMYYYY));
        weekPage.setDate(currentDate.getSelectedDate());
    }

    /**
     * Changes the year view by the given value
     * @param year
     */
    private void changeYear(int year) {
        currentDate.setPlusYear(year);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.YYYY));
        yearView.setDate(currentDate.getSelectedDate());
    }

}


